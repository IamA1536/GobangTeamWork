package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server implements Runnable{

	public static final int ROWS = 15;//����
	public static final int COLUMNS = 15;//����
	static int[][] chesses = new int[ROWS][COLUMNS];
	private InputStream is;
	private OutputStream os;
	private boolean flag = false;//�����һ�κ�Ϊtrue,����һ������,�ٱ�Ϊfalse
	private int row;//�������ӵ��У�ÿ�ε����ı�
	private int col;//���ӵ���
	private GoBang gobang;
	
//	public static void main(String[] args) {
//		Server server = new Server();
//		server.init();
//		Thread th = new Thread(server);
//		th.start();
//	}
	
	public Server(GoBang gobang){
		this.gobang = gobang;
		try {
			System.out.println("Open Port");
			ServerSocket ss = new ServerSocket(8080);
			System.out.println("Set Port");
			Socket socket = ss.accept();

//			System.out.println("Open Complete");
			is = socket.getInputStream();
			os = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
	//����������������������
	public void setLocation(int row,int col){
		this.row = row;
		this.col = col;
		flag = true;
	}
	
	public int[][] getChesses(){
		return chesses;
	}

	public void run(){
		try{
			Thread.sleep(1000);
			while(true){
				while(true){
					try{
						byte[] bytes = new byte[1024];
			            is.read(bytes);
			            String s = new String(bytes);
			            String[] split = s.split(",");
			            int r = Integer.parseInt(split[0]);
			            int c = Integer.parseInt(split[1].trim());//trim()����ȥ����β�ո�
						System.out.println("���ܳɹ���������������Ϊ:"+r+","+c);
						gobang.AddPiece(r,c);
						chesses[r][c] = 1;;//��������
						break;
					}catch (Exception ex) {
							System.out.println("������Ϣ����"+ex.getMessage());
							ex.printStackTrace();
							continue;
						}
				}
				while(true){
					if(!flag){
						continue;
					}
					String location = row+","+col;
					if(row >= 0 && row < ROWS && col >= 0 && col <= COLUMNS && chesses[row][col] == 0){
						chesses[row][col] = -1;
						PrintWriter pw = new PrintWriter(os);
						os.write(location.getBytes());
						os.flush();
						System.out.println("���ͳɹ�:"+location);
						pw.flush();
						flag = false;
						break;
					}else{
						System.out.println("�����������������!");
						continue;
					}
				}	
			}
		}catch(Exception ex){
				ex.printStackTrace();
				System.out.println("����ʧ��");
			}
	}

}