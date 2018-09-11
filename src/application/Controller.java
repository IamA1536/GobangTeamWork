package application;

import java.awt.Point;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import com.AWork.Fuction.AI.AIBoard.Board;
import com.AWork.Fuction.AI.AIBrain.Brain;
import com.AWork.Piece.Class.Piece;
import com.TeamWork.Data.AI_Info.*;
import com.TeamWork.Data.Global.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller implements Initializable, Data {
    private static final int Hard = 0;
    Server server;
    Client client;
    Thread th;
    private boolean IsConnected = false;
    Stage OnlyStage;
    Controller CONTR;// ����ȡIP�Ͷ˿�ʱʹ��
    Scene scene;
    YourTime yourtime;

    @FXML
    TextField NAME;

    @FXML
    Label Name_Gamer;

    @FXML
    MenuButton Difficulty;

    @FXML
    Label Time;

    @FXML
    TextField IP;

    @FXML
    BorderPane GAME;

    GoBang gobang = new GoBang();

    String ip;
    int port = 8080;
    String name;

    public Controller() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub

    }

    public void SetStage(Stage Only) {
        this.OnlyStage = Only;
    }

    public void SetController(Controller con) {
        this.CONTR = con;
        // ͬ�ϣ�����ȡIP�Ͷ˿�ʱ����
    }

    public void ShowMENU() throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MENU.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1248, 846);
            Controller controller = fxmlLoader.getController();
            controller.SetStage(OnlyStage);
            if (IsConnected) {
                IsConnected = false;
                th.stop();
            }
            OnlyStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowLocal() throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Local.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1248, 846);
            Controller controller = fxmlLoader.getController();
            controller.Difficulty.setVisible(false);// �Ѷ�ѡ�񲻿ɼ�
            controller.gobang = new GoBang();
            controller.GAME.setLeft(controller.gobang.c);
            controller.SetStage(OnlyStage);
            OnlyStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowGetName() throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GetName.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1248, 846);
            Controller controller = fxmlLoader.getController();
            controller.SetStage(OnlyStage);
            OnlyStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowOnline() throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Online.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1248, 846);
            Controller controller = fxmlLoader.getController();
            controller.SetStage(OnlyStage);
            controller.yourtime = new YourTime();
            controller.yourtime.setLabel(controller.Time);
            controller.yourtime.ReFresh();
            controller.gobang = new GoBang();
            controller.GAME.setLeft(controller.gobang.c);
            controller.name = this.name;
            controller.Name_Gamer.setText("�������\n" + name);
            controller.Name_Gamer.setContentDisplay(ContentDisplay.CENTER);
            OnlyStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShowGetIP() throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GetIP.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            Controller controller = fxmlLoader.getController();
            controller.SetStage(new Stage());
            controller.OnlyStage.setTitle("Get IP");
            controller.OnlyStage.setResizable(false);
            controller.SetController(this);
            controller.OnlyStage.setScene(scene);
            controller.OnlyStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EXIT() {
        OnlyStage.close();
    }

    public void EXITGame() {
        if (IsConnected) {
            IsConnected = false;
            th.stop();
        }
        OnlyStage.close();
    }

    public void SUB() throws Exception {
        name = NAME.getText();
        if (name.length() != 0) {
            ShowOnline();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("����");
            alert.setHeaderText("�������Ϲ淶");
            alert.setContentText("��������Ϊ��");
            alert.showAndWait();
        }
    }

    public void LocalPVP() {

        gobang.thatstart = true;
        Difficulty.setVisible(false);
        if (gobang.IsAI)
            gobang.bd.reset();
        gobang.IsAI = false;
        // ʣ������д����PVP��ʵ��
    }

    public void LocalPVE() {
        Difficulty.setVisible(true);
        // �Ѷ�ѡ�񣬿ɼ�
        // PVEʵ����������Ѷ�ѡ����
    }

    public void LocalUNDO() {

        if (gobang.totalgonumber < 2) {
            gobang.g.save();
            gobang.g.setFill(Color.BURLYWOOD);
            gobang.g.fillRect(91, 50, 800, 750);

            for (int i = 0; i < 14; i++) {
                for (int j = 0; j < 14; j++) {
                    if ((i + 1) % 2 != 0 && (j + 1) % 2 != 0)
                        gobang.g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                    else if ((i + 1) % 2 == 0 && (j + 1) % 2 == 0)
                        gobang.g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                    else
                        gobang.g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                }
            }
            for (int i = 0; i < gobang.totalgonumber - 1; i++) {
                if (gobang.golist[i].getColorPi() == 2)
                    gobang.g.setFill(Color.WHITE);
                else
                    gobang.g.setFill(Color.BLACK);
                gobang.g.fillOval(gobang.changeX(gobang.golist[i].getX()) - 20,
                        gobang.changeY(gobang.golist[i].getY()) - 20, 40, 40);
            }

            gobang.golist[gobang.totalgonumber].setColorPi(emptyPiece);
            gobang.totalgonumber -= 1;
        } else {
            gobang.g.save();
            gobang.g.setFill(Color.BURLYWOOD);
            gobang.g.fillRect(91, 50, 800, 750);

            for (int i = 0; i < 14; i++) {
                for (int j = 0; j < 14; j++) {
                    if ((i + 1) % 2 != 0 && (j + 1) % 2 != 0)
                        gobang.g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                    else if ((i + 1) % 2 == 0 && (j + 1) % 2 == 0)
                        gobang.g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                    else
                        gobang.g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                }
            }
            System.out.println(gobang.totalgonumber);
            Point a1 = gobang.bd.undo();
            for (int i = 0; i < gobang.totalgonumber - 2; i++) {
                if (gobang.golist[i].getColorPi() != 1)
                    gobang.g.setFill(Color.WHITE);
                else
                    gobang.g.setFill(Color.BLACK);

                gobang.g.fillOval(gobang.changeX(gobang.golist[i].getX()) - 20,
                        gobang.changeY(gobang.golist[i].getY()) - 20, 40, 40);
                System.out.println(
                        gobang.changeX(gobang.golist[i].getX()) + "  " + gobang.changeY(gobang.golist[i].getY()));
            }
            gobang.totalgonumber -= 2;
        }
    }

    // ���ػ���

    public void SimplePVE() {
        gobang.IsAI = true;
        gobang.thatstart = true;
        gobang.bd = new Board();
        gobang.br = new Brain(gobang.bd, 2, 3);
        gobang.bd.start();
        int[] best = gobang.br.AIMove(Easy);
        gobang.golist[gobang.totalgonumber] = new Piece(8, 8, gobang.totalgonumber % 2 + 1);
        if (gobang.golist[gobang.totalgonumber].getColorPi() != 1)
            gobang.g.setFill(Color.WHITE);
        else
            gobang.g.setFill(Color.BLACK);
        gobang.g.fillOval(gobang.changeX(gobang.golist[gobang.totalgonumber].getX()) - 20,
                gobang.changeY(gobang.golist[gobang.totalgonumber].getY()) - 20, 40, 40);
        gobang.totalgonumber += 1;
        // ��PVE
    }

    public void NormalPVE() {
        gobang.IsAI = true;
        gobang.thatstart = true;
        gobang.bd = new Board();
        gobang.br = new Brain(gobang.bd, 3, 4);
        gobang.bd.start();
        gobang.piece[CENTER - 1][CENTER - 1] = 1;
        int[] best = gobang.br.AIMove(Easy);
        gobang.golist[gobang.totalgonumber] = new Piece(8, 8, gobang.totalgonumber % 2 + 1);
        if (gobang.golist[gobang.totalgonumber].getColorPi() != 1)
            gobang.g.setFill(Color.WHITE);
        else
            gobang.g.setFill(Color.BLACK);
        gobang.g.fillOval(gobang.changeX(gobang.golist[gobang.totalgonumber].getX()) - 20,
                gobang.changeY(gobang.golist[gobang.totalgonumber].getY()) - 20, 40, 40);
        gobang.totalgonumber += 1;
        // ��ͨPVE
    }

    public void HardPVE() {
        gobang.IsAI = true;
        gobang.thatstart = true;
        gobang.bd = new Board();
        gobang.br = new Brain(gobang.bd, 3, 5);
        gobang.bd.start();
        int[] best = gobang.br.AIMove(Hard);
        gobang.golist[gobang.totalgonumber] = new Piece(8, 8, gobang.totalgonumber % 2 + 1);
        if (gobang.golist[gobang.totalgonumber].getColorPi() != 1)
            gobang.g.setFill(Color.WHITE);
        else
            gobang.g.setFill(Color.BLACK);
        gobang.g.fillOval(gobang.changeX(gobang.golist[gobang.totalgonumber].getX()) - 20,
                gobang.changeY(gobang.golist[gobang.totalgonumber].getY()) - 20, 40, 40);
        gobang.totalgonumber += 1;
        // ����PVE
    }

    public void TimeStart() {
        yourtime.start();
    }

    public void TimeStop() {
        yourtime.stop();
    }

    public void BlackWin() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Black Win");
        alert.setHeaderText("�ֳ�ʤ��");
        alert.setContentText("����ʤ");
        alert.showAndWait();
    }

    public void WhiteWin() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("White Win");
        alert.setHeaderText("�ֳ�ʤ��");
        alert.setContentText("����ʤ");
        alert.showAndWait();
    }


    public void SUBIP() {
        ip = IP.getText();
        if (ip.length() == 0) {
            System.out.println("StartServer");
            server = new Server(CONTR.gobang);
            IsConnected = true;
            CONTR.gobang.setConnect(true);
            CONTR.gobang.setServer(server);
            CONTR.gobang.Type = 1;
            System.out.println("StartServer");
            th = new Thread(server);
            th.start();
            this.EXIT();
        } else {
            client = new Client(ip, CONTR.gobang);

//            CONTR.gobang.Type = 2;
            th = new Thread(CONTR.gobang.client);
            th.start();
        }

//         ����IP�Ͷ˿�ΪĿ������

        if (client.init()) {//�ж��Ƿ����ӳɹ�
            CONTR.ip = this.ip;
            IsConnected = true;
            CONTR.gobang.setClient(client);
            CONTR.gobang.Type = 2;
            this.EXIT();
        }
    }

    public void GetMyIP() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Your IP");
        alert.setHeaderText("����IP��ַΪ(��������Ĭ��Ϊ����)");
        alert.setContentText(addr.toString());
        alert.showAndWait();
    }
}