package application;

import javafx.scene.control.Alert;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;

public class YourTime extends Thread{
	private int time=0;
	private Label TEXT;
	public void run() {
		while(true) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			time++;
			ReFresh();
			if(time>=600) {
				Alert alert=new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("YouFail");
				alert.setHeaderText("���ѳ�ʱ");
				alert.setContentText("������");
				alert.showAndWait();
				break;
			}
		}
	}
	
	public void setLabel(Label label) {
		this.TEXT=label;
	}
	
	public int getTime() {
		return time;
	}
	
	public void ReFresh() {
		String TIME=String.valueOf(time);
		TEXT.setText(TIME);
		TEXT.setContentDisplay(ContentDisplay.CENTER);
	}
}
