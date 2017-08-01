import java.awt.List;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * �������һ����Hash�㷨
 * ���������������µ�ƽ��������
 */

public class ConsistentHashingWithVirtualNode {

	/**
     * �������Hash���ķ������б�
	 */
	private static String[] servers = {"192.168.0.0:111", "192.168.0.1:111", 
			"192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111"};
	
	/**
	 * ��ʵ����б�,���ǵ����������ߡ����ߵĳ���������ӡ�ɾ���ĳ�����Ƚ�Ƶ��������ʹ��LinkedList�����
	 */
	private static LinkedList<String> realNodes = new LinkedList<>();
	
     /**
      * key��ʾ��������hashֵ��value��ʾ������������
      */
     private static SortedMap<Integer, String> virtualNodes = 
             new TreeMap<Integer, String>();
     
     /**
      * ���������Ŀ������д������һ������Ӧ5��������
      */
     private static final int VIRTUAL_NODES = 5;
     
     /**
      * �����ʼ���������еķ���������sortedMap��
      */
     static
     {
    	 //�Ȱ�ԭʼ�ķ�������ӵ���ʵ�����б���
    	 for(int i=0; i<servers.length; i++)
    		 realNodes.add(servers[i]);
    	 
    	// ���������ڵ㣬����LinkedListʹ��foreachѭ��Ч�ʻ�Ƚϸ�
         for(String str : realNodes) {
        	 for(int i=0; i<VIRTUAL_NODES; i++) {
        		 String virtualNodeName = str + "&&VN" + String.valueOf(i);
        		 int hash = getHash(virtualNodeName);
        		 System.out.println("[" + servers[i] + "]���뼯����, ��HashֵΪ" + hash);
        		 virtualNodes.put(hash, virtualNodeName);
        	 }
        	 System.out.println();
         } 
     }
     
     /**
      * ʹ��FNV1_32_HASH�㷨�����������Hashֵ,���ﲻʹ����дhashCode�ķ���������Ч��û���� 
      */
     private static int getHash(String str)
     {
         final int p = 16777619;
         int hash = (int)2166136261L;
         for (int i = 0; i < str.length(); i++)
             hash = (hash ^ str.charAt(i)) * p;
         
         hash += hash << 13;
         hash ^= hash >> 7;
         hash += hash << 3;
         hash ^= hash >> 17;
         hash += hash << 5;
         
         // ����������ֵΪ������ȡ�����ֵ
         if (hash < 0)
             hash = Math.abs(hash);
         return hash;
     }
     
     /**
      * �õ�Ӧ��·�ɵ��Ľ��
      */
     private static String getServer(String node)
     {
         // �õ���·�ɵĽ���Hashֵ
         int hash = getHash(node);
         // �õ����ڵ��ڸ�Hashֵ������Map
         SortedMap<Integer, String> subMap = 
                 virtualNodes.tailMap(hash);
         // ��һ��Key����˳ʱ���ȥ��node������Ǹ����
         Integer i = subMap.firstKey();
         // ���ض�Ӧ������ڵ����ƣ������ַ�����΢��ȡһ��
         String virtualNode = subMap.get(i);
         return virtualNode.substring(0, virtualNode.indexOf("&&"));
     }
     
     public static void main(String[] args)
     {
         String[] nodes = {"127.0.0.1:1111", "221.226.0.1:2222", "10.211.0.1:3333"};
         for (int i = 0; i < nodes.length; i++)
             System.out.println("[" + nodes[i] + "]��hashֵΪ" + 
                     getHash(nodes[i]) + ", ��·�ɵ����[" + getServer(nodes[i]) + "]");
     }

}
