# coding:utf-8

import logging
import urlparse
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import os
from logging.handlers import TimedRotatingFileHandler


# 客户端白名单，白名单中的机器可以访问该服务
WHITE_LIST = ['127.0.0.1']

# 操作日志记录
# 1、logging.basicConfig(level, format, datefmt, filename, filemode)
#   level: 设置日志级别，默认为logging.WARNING，NOTSET/DEBUG/INFO/WARNING/ERROR/CRITICAL
#   format: 指定输出的格式和内容，format可以输出很多有用信息【'%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s'】
#   datefmt: 指定时间格式，【'%a, %d %b %Y %H:%M:%S'】
#   filename: 指定日志文件件
#   filemode: 指定日志文件的打开模式，'w'或'a'
logging.basicConfig(
    level=logging.DEBUG,
    format = '%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',  # 定义输出log的格式
    datefmt = '%Y%m%d %A %H:%M:%S',  # 时间
    filename = os.path.join(os.getcwd(), 'log.txt'),
    filemode = 'a')
# 添加 TimedRotatingFileHandler
# 2、TimedRotatingFileHandler(filename [,when [,interval [,backupCount]]])
#   filename 是输出日志文件名的前缀，比如log/myapp.log
#   when 是一个字符串的定义如下：
#     “S”: Seconds
#     “M”: Minutes
#     “H”: Hours
#     “D”: Days
#     “W”: Week day (0=Monday)
#     “midnight”: Roll over at midnight
#   interval 是指等待多少个单位when的时间后，Logger会自动重建文件
#   backupCount 是保留日志个数
log_file_handler = TimedRotatingFileHandler(
    filename = os.path.join(os.getcwd(), 'log.txt'),
    when = "D",
    interval = 1,
    backupCount = 60)
logging.getLogger().addHandler(log_file_handler)  # 实例化添加handler
# 添加控制台显示日志
console = logging.StreamHandler();
console.setLevel(logging.DEBUG);
formatter = logging.Formatter('%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s');
console.setFormatter(formatter);
logging.getLogger('').addHandler(console);

# 词典id
file_no_list = ["0", "1"]
# 允许的操作，1:增加词 2:删除词
operation_list = ["1", "2"]
# 词典映射
file = {}
file['0'] = "test1.txt"
file['1'] = "test2.txt"

class MyRequestHandler(BaseHTTPRequestHandler):
    # 请求响应
    def _write_resp(self, resp_code, msg):
        self.send_response(resp_code)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        self.wfile.write(str(self.headers))
        self.wfile.write(msg)


    # Get请求的处理
    def do_GET(self):
        logging.info("Client %s request service, method %s, params %s", self.client_address, self.command, self.path)
        if self.client_address[0] not in WHITE_LIST:
            logging.warning("Client has no access right.")
            self._write_resp(403, "Please check access rights.")
            return
        datas = self.url2Dict(self.path)
        flag, file_no, operation, words = self.check_params(datas)
        if not flag:
            logging.warning("Request params error.")
            self._write_resp(400, "Please check request params.")
            return
        res_code, msg = self.modify_Dict(file_no, operation, words)
        self._write_resp(res_code, msg)


    # url解析，将url解析转换成字典
    def url2Dict(self, url):
        query = urlparse.urlparse(url).query
        return dict([(k, v[0]) for k, v in urlparse.parse_qs(query).items()])


    # 参数校验
    def check_params(self, datas):
        file_no = datas.get("file_no", -1)
        if file_no == -1 or file_no not in file_no_list:
            logging.debug("Request params error --- file_no error.")
            return False, "", "", []
        operation = datas.get("operation", -1)
        if operation == -1 or operation not in operation_list:
            logging.debug("Request params error --- operation error.")
            return False, file_no, "", []
        words_tmp = datas.get("words", -1)
        if words_tmp == -1:
            logging.debug("Request params error --- words error.")
            return False, file_no, operation, []
        words = words_tmp.split(",")
        if len(words) < 1:
            logging.debug("Request params error --- words error.")
            return False, file_no, operation, []
        return True, file_no, operation, words


    # 更新词库操作
    def modify_Dict(self, file_no, operation, words):
        url = "git@github.com:xingzhexiaozhu/UsefulProgram.git"
        file_path = os.path.abspath(".") + "/UsefulProgram/"
        data_path = file_path + file[file_no]

        # 拉取最新代码
        if not self.git_init(url, file_path):
            logging.error("Git init error.")
            return 500, "git init error."
        # 更新词库
        if not self.modify(data_path, operation, words):
            logging.error("Modify dict error.")
            return 500, "modify dict error."
        # 提交修改
        if not self.git_push(file_path, data_path):
            logging.error("Git push error.")
            return 500, "git push error."
        logging.debug("Everything done.")
        return 200, "Success."


    # 初始化操作
    def git_init(self, url, file_path):
        try:
            if os.path.exists(file_path): # 拉取过则每次更新前先 git pull
                os.system("git -C " + file_path + " pull origin master")
                logging.debug("git pull %s", file_path)
            else: # 否则更新前需要先 git clone
                os.system("git clone " + url)
                logging.debug("git clone %s", url)
            return True
        except:
            logging.error("git init error")
            return False



    # 更新词库
    def modify(self, data_path, operation, words):
        try:
            # 增加词
            if operation == '1':
                self.add_words(data_path, words)
            # 删除词
            elif operation == '2':
                self.del_words(data_path, words)
            logging.debug("Modify dict done.")
            return True
        except:
            logging.error("Modify dict error")
            return False


    # 修改完成后提交修改到对应的分支
    def git_push(self, file_path, data_path):
        try:
            path = os.getcwd()  # 获取当前路径
            os.chdir(file_path) # 进入要提交的仓库
            print(os.getcwd())
            print(file_path)
            print(data_path)
            os.system("git add " + data_path)
            os.system("git commit -m " + "update")
            os.system("git push origin master")
            os.getcwd()
            os.chdir(path)      # 返回之前的路径
            logging.debug("Git push done.")
            return True
        except:
            logging.error("git push error")
            return False


    # 增加词到词库
    def add_words(self, data_path, words):
        with open(data_path, 'a+') as file:
            for word in words:
                if len(word.strip()) != 0:
                    logging.info("insert word %s in file %s", word, data_path)
                    file.write(word)
                    file.write("\n")


    # 从词库中删除词
    def del_words(self, data_path, words):
        tmp_file = data_path + ".tmp"
        write_file = open(tmp_file, 'w')
        with open(data_path, 'r') as read_file:
            while True:
                line = read_file.readline()
                if line != '':
                    if len(line.strip()) != 0:
                        if line.strip() in words:
                            logging.info("delete word %s from %s", line, data_path)
                            continue
                        write_file.write(line)
                else:
                    break
        write_file.close()
        os.system("cp -rf " + tmp_file + " " + data_path)
        os.system("rm " + tmp_file)


if __name__=="__main__":
    server = HTTPServer(('', 8899), MyRequestHandler)
    print("Started http server ...")
    server.serve_forever()