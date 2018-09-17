package model1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.PriorityQueue;

//若字符频率的字节大于 2^32 可能导致问题
public class Hzipping {

    static PriorityQueue<TREE> pq = new PriorityQueue<TREE>();//PriorityQueue是自定义一种优先级的方式，每次输出是最高优先级的数据，而不是第一次进入的数据
    static int[] freq = new int[300];//字符频率
    static String[] ss = new String[300];
    static int exbits;//写入压缩文件的字符数量
    static byte bt;//字符对应的字节
    static int cnt; // 不同字符的数量


    static class TREE implements Comparable<TREE> {

        TREE Lchild;//左子树
        TREE Rchild;//右子树
        public String deb;
        public int Bite;
        public int Freqnc;//字符频率

        /*
         *字符频率比较
         */
        public int compareTo(TREE T) {
            if (this.Freqnc < T.Freqnc) {
                return -1;
            }
            if (this.Freqnc > T.Freqnc) {
                return 1;
            }
            return 0;
        }
    }

    static TREE Root;

    /*
     *计算每个字符出现的频率
     */
    public static void CalFreq(String fname) {
        File file = null;
        Byte bt;

        file = new File(fname);
        try {
            FileInputStream file_input = new FileInputStream(file);//从文件中读取字节
            DataInputStream data_in = new DataInputStream(file_input);//数据输入流，从底层输入流中读取基本 Java 数据类型
            while (true) {
                try {

                    bt = data_in.readByte();
                    /*readByte()方法读取并返回一个单一的输入字节。
                      该字节的范围是从-128到127之间的有符号值(8位byte)，在返回异常前一直读取*/
                    freq[convert(bt)]++;
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }
            file_input.close();
            data_in.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        file = null;
    }

    /*
     * 字节向二进制编码转换
     */
    public static int convert(Byte b) {
        int ret = b;
        if (ret < 0) {
            ret = ~b;//位非运算
            ret = ret + 1;
            //按位取反，末位加1，得补码
            ret = ret ^ 255;//位异或运算，相同为0，不同为1
            //相当于加密，之后再进行一次ret^255 即可得出原来的ret
            ret += 1;
        }
        return ret;
    }

    /*
     * 深度优先遍历释放内存
     */
    public static void fredfs(TREE now) {

        if (now.Lchild == null && now.Rchild == null) {
            now = null;
            return;
        }
        if (now.Lchild != null) {
            fredfs(now.Lchild);
        }
        if (now.Rchild != null) {
            fredfs(now.Rchild);
        }
    }

    /*
     *释放内存函数
     */
    public static void initHzipping() {
        int i;
        cnt = 0;
        if (Root != null) {
            fredfs(Root);
        }
        for (i = 0; i < 300; i++) {
            freq[i] = 0;
        }
        for (i = 0; i < 300; i++) {
            ss[i] = "";
        }
        pq.clear();//从此优先级队列中移除所有元素
    }


    /*
     *深度优先遍历构造节点
     */
    public static void dfs(TREE now, String st) {
        now.deb = st;
        if ((now.Lchild == null) && (now.Rchild == null)) {
            ss[now.Bite] = st;
            return;
        }
        if (now.Lchild != null) {
            dfs(now.Lchild, st + "0");
        }
        if (now.Rchild != null) {
            dfs(now.Rchild, st + "1");
        }
    }


    /*
     * 赋予所有节点优先权，构造二叉树
     */
    //构造每个字符对应的节点
    public static void MakeNode() {
        int i;
        pq.clear();

        for (i = 0; i < 300; i++) {
            if (freq[i] != 0) {
                TREE Temp = new TREE();
                Temp.Bite = i;
                Temp.Freqnc = freq[i];
                Temp.Lchild = null;
                Temp.Rchild = null;
                pq.add(Temp);//将指定的元素插入此优先级队列
                cnt++;
            }

        }
        TREE Temp1, Temp2;

        if (cnt == 0) {
            return;
        } else if (cnt == 1) {
            for (i = 0; i < 300; i++) {
                if (freq[i] != 0) {
                    ss[i] = "0";
                    break;
                }
            }
            return;
        }

        // 若文件为空可能导致出错
        while (pq.size() != 1) {
            TREE Temp = new TREE();
            Temp1 = pq.poll();//获取并移除此队列的头，如果此队列为空，则返回 null
            Temp2 = pq.poll();
            Temp.Lchild = Temp1;
            Temp.Rchild = Temp2;
            Temp.Freqnc = Temp1.Freqnc + Temp2.Freqnc;
            pq.add(Temp);
        }
        Root = pq.poll();
    }


    /*
     * fakezip 生成"fakezipped.txt" 将把最终的二进制编码放入realzip 文件中
     * 读取文件，做压缩准备
     */
    public static void fakezip(String fname) {

        File filei, fileo;

        filei = new File(fname);
        fileo = new File("fakezipped.txt");
        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            PrintStream ps = new PrintStream(fileo);

            while (true) {
                try {
                    bt = data_in.readByte();
                    ps.print(ss[convert(bt)]);
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }

            file_input.close();
            data_in.close();
            ps.close();

        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        filei = null;
        fileo = null;

    }

    /*
     * 根据"fakezipped.txt"的编码进行压缩
     */
    public static void realzip(String fname, String fname1) {
        File filei, fileo;
        int i, j = 10;
        Byte btt;

        filei = new File(fname);
        fileo = new File(fname1);

        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            FileOutputStream file_output = new FileOutputStream(fileo);
            DataOutputStream data_out = new DataOutputStream(file_output);

            data_out.writeInt(cnt);//压缩汉字时的字符出现范围是0~255 
            for (i = 0; i < 256; i++) {
                if (freq[i] != 0) {
                    btt = (byte) i;
                    data_out.write(btt);
                    data_out.writeInt(freq[i]);
                }
            }
            long texbits;//满8位写入文件中，最后一个字节没写满8位也要写入文件中
            texbits = filei.length() % 8;
            texbits = (8 - texbits) % 8;
            exbits = (int) texbits;
            data_out.writeInt(exbits);//writeInt把一个字符的所有位写到输出流
            while (true) {
                try {
                    bt = 0;
                    byte ch;
                    for (exbits = 0; exbits < 8; exbits++) {
                        ch = data_in.readByte();
                        bt *= 2;
                        if (ch == '1') {
                            bt++;
                        }
                    }
                    data_out.write(bt);//write把最低8位写到输出流

                } catch (EOFException eof) {
                    int x;
                    if (exbits != 0) {
                        for (x = exbits; x < 8; x++) {
                            bt *= 2;
                        }
                        data_out.write(bt);
                    }

                    exbits = (int) texbits;
                    System.out.println("extrabits: " + exbits);
                    System.out.println("End of File");
                    break;
                }
            }
            data_in.close();
            data_out.close();
            file_input.close();
            file_output.close();
            System.out.println("output file's size: " + fileo.length());

        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }
        filei.delete();
        filei = null;
        fileo = null;
    }

    public static void beginHzipping(String arg1) {
        initHzipping();
        CalFreq(arg1); //计算每个字符的频率
        MakeNode(); // 构造每个字符对应的节点
        if (cnt > 1) {
            dfs(Root, ""); // 深度优先遍历构造节点
        }
        fakezip(arg1); // 把二进制编码写入"fakezipped.txt"
        realzip("fakezipped.txt", arg1 + ".huffz"); // 压缩
        initHzipping();
    }
}
