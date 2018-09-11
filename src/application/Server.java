package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server implements Runnable{

	public static final int ROWS = 15;//行数
	public static final int COLUMNS = 15;//列数
	static int[][] chesses = new int[ROWS][COLUMNS];
	private InputStream is;
	private OutputStream os;
	private boolean flag = false;//鼠标点击一次后为true,发送一次坐标,再变为false
	private int row;//储存棋子的行，每次点击后改变
	private int col;//棋子的列
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
	//鼠标点击后调用设置棋子坐标
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
			            int c = Integer.parseInt(split[1].trim());//trim()函数去除首尾空格
						System.out.println("接受成功，对手下棋坐标为:"+r+","+c);
						gobang.AddPiece(r,c);
						chesses[r][c] = 1;;//存入棋子
						break;
					}catch (Exception ex) {
							System.out.println("接受消息出错："+ex.getMessage());
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
						System.out.println("发送成功:"+location);
						pw.flush();
						flag = false;
						break;
					}else{
						System.out.println("坐标错误，请重新输入!");
						continue;
					}
				}	
			}
		}catch(Exception ex){
				ex.printStackTrace();
				System.out.println("发送失败");
			}
	}

}