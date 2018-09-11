package application;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javafx.scene.control.Alert;


public class Client implements Runnable{
	public static final int ROWS = 15;//����
	public static final int COLUMNS = 15;//����
	static int[][] chesses = new int[ROWS][COLUMNS];
	private String ip;
	private int port = 8080;//�˿�
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private boolean flag = false;//�����һ�κ�Ϊtrue,����һ������,�ٱ�Ϊfalse
	private int row;//�������ӵ��У�ÿ�ε����ı�
	private int col;//���ӵ���
	private GoBang gobang;
	
//	public static void main(String[] args) {
//		Client client = new Client();
//		Thread th = new Thread(client);
//		th.start();
//	}

	public Client(String ip,GoBang gobang){
		//UIʡ��
		this.ip = ip;
		this.gobang = gobang;
		//���ӷ����������������߳�
	}

	public boolean init(){
		try {
			socket = new Socket(ip,port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			System.out.println("���ӳɹ�");
			return true;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("����");
			alert.setHeaderText("���Ӵ���");
			alert.setContentText("���������ӱ��ܾ����߳�ʱ");
			alert.showAndWait();
			return false;
		}
	}
	
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
			}
		}catch(Exception ex){
				ex.printStackTrace();
				System.out.println("����ʧ��");
			}
	}
}
