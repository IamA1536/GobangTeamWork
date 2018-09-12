package com.TeamWork.Gobang.GobangMain;

import java.awt.Point;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import com.AWork.Fuction.AI.AIBoard.Board;
import com.AWork.Fuction.AI.AIBrain.Brain;
import com.AWork.Piece.Class.Piece;
import com.LCWork.NetWork.Client;
import com.LCWork.NetWork.Server;
import com.LNWork.Addition.GoBang;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * javaFX控制器
 * @author YZQ
 * @version 2.0
 * @since 1.0
 */

public class Controller implements Initializable, Data {
    private static final int Hard = 0;
    Server server;
    Client client;
    Thread th;
    private boolean IsConnected = false;
    Stage OnlyStage;
    Controller CONTR;// 仅获取IP和端口时使用
    Scene scene;

    @FXML
    TextField NAME;

    @FXML
    Label Name_Gamer;

    @FXML
    MenuButton Difficulty;

    @FXML
    TextField IP;

    @FXML
    BorderPane GAME;

    @FXML
    Pane GetIP;

    @FXML
    Pane Connecting;
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
        // 同上，仅获取IP和端口时调用
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
            controller.Difficulty.setVisible(false);// 难度选择不可见
            controller.gobang = new GoBang();
            controller.GAME.setLeft(controller.gobang.getC());
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
            controller.gobang = new GoBang();
            controller.GAME.setLeft(controller.gobang.getC());
            controller.name = this.name;
            controller.Name_Gamer.setText("你的名字\n" + name);
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
            controller.Connecting.setVisible(false);
            controller.Connecting.setManaged(false);
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
            alert.setTitle("警告");
            alert.setHeaderText("姓名不合规范");
            alert.setContentText("姓名不可为空");
            alert.showAndWait();
        }
    }

    public void LocalPVP() {

        gobang.thatstart = true;
        Difficulty.setVisible(false);
        if (gobang.IsAI)
            gobang.bd.reset();
        gobang.IsAI = false;
        // 剩下请填写本地PVP的实现
    }

    public void LocalPVE() {
        Difficulty.setVisible(true);
        // 难度选择，可见
        // PVE实现在下面的难度选择中
    }

    public void LocalUNDO() {

        if (gobang.totalgonumber < 2) {
            gobang.getG().save();
            gobang.getG().setFill(Color.BURLYWOOD);
            gobang.getG().fillRect(91, 50, 800, 750);

            for (int i = 0; i < 14; i++) {
                for (int j = 0; j < 14; j++) {
                    if ((i + 1) % 2 != 0 && (j + 1) % 2 != 0)
                        gobang.getG().strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                    else if ((i + 1) % 2 == 0 && (j + 1) % 2 == 0)
                        gobang.getG().strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                    else
                        gobang.getG().strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                }
            }
            for (int i = 0; i < gobang.totalgonumber - 1; i++) {
                if (gobang.getGolist()[i].getColorPi() == 2)
                    gobang.getG().setFill(Color.WHITE);
                else
                    gobang.getG().setFill(Color.BLACK);
                gobang.getG().fillOval(gobang.changeX(gobang.getGolist()[i].getX()) - 20,
                        gobang.changeY(gobang.getGolist()[i].getY()) - 20, 40, 40);
            }

            gobang.getGolist()[gobang.totalgonumber].setColorPi(emptyPiece);
            gobang.totalgonumber -= 1;
        } else {
            gobang.getG().save();
            gobang.getG().setFill(Color.BURLYWOOD);
            gobang.getG().fillRect(91, 50, 800, 750);

            for (int i = 0; i < 14; i++) {
                for (int j = 0; j < 14; j++) {
                    if ((i + 1) % 2 != 0 && (j + 1) % 2 != 0)
                        gobang.getG().strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                    else if ((i + 1) % 2 == 0 && (j + 1) % 2 == 0)
                        gobang.getG().strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                    else
                        gobang.getG().strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                }
            }
            System.out.println(gobang.totalgonumber);
            Point a1 = gobang.bd.undo();
            for (int i = 0; i < gobang.totalgonumber - 2; i++) {
                if (gobang.getGolist()[i].getColorPi() != 1)
                    gobang.getG().setFill(Color.WHITE);
                else
                    gobang.getG().setFill(Color.BLACK);

                gobang.getG().fillOval(gobang.changeX(gobang.getGolist()[i].getX()) - 20,
                        gobang.changeY(gobang.getGolist()[i].getY()) - 20, 40, 40);
                System.out.println(
                        gobang.changeX(gobang.getGolist()[i].getX()) + "  " + gobang.changeY(gobang.getGolist()[i].getY()));
            }
            gobang.totalgonumber -= 2;
        }
    }

    // 当地悔棋

    public void SimplePVE() {
        gobang.IsAI = true;
        gobang.thatstart = true;
        gobang.bd = new Board();
        gobang.br = new Brain(gobang.bd, 2, 3);
        gobang.bd.start();
        int[] best = gobang.br.AIMove(Easy);
        gobang.getGolist()[gobang.totalgonumber] = new Piece(8, 8, gobang.totalgonumber % 2 + 1);
        if (gobang.getGolist()[gobang.totalgonumber].getColorPi() != 1)
            gobang.getG().setFill(Color.WHITE);
        else
            gobang.getG().setFill(Color.BLACK);
        gobang.getG().fillOval(gobang.changeX(gobang.getGolist()[gobang.totalgonumber].getX()) - 20,
                gobang.changeY(gobang.getGolist()[gobang.totalgonumber].getY()) - 20, 40, 40);
        gobang.totalgonumber += 1;
        Difficulty.setVisible(false);
        // 简单PVE
    }

    public void NormalPVE() {
        gobang.IsAI = true;
        gobang.thatstart = true;
        gobang.bd = new Board();
        gobang.br = new Brain(gobang.bd, 3, 5);
        gobang.bd.start();
        gobang.getPiece()[CENTER - 1][CENTER - 1] = 1;
        int[] best = gobang.br.AIMove(Easy);
        gobang.getGolist()[gobang.totalgonumber] = new Piece(8, 8, gobang.totalgonumber % 2 + 1);
        if (gobang.getGolist()[gobang.totalgonumber].getColorPi() != 1)
            gobang.getG().setFill(Color.WHITE);
        else
            gobang.getG().setFill(Color.BLACK);
        gobang.getG().fillOval(gobang.changeX(gobang.getGolist()[gobang.totalgonumber].getX()) - 20,
                gobang.changeY(gobang.getGolist()[gobang.totalgonumber].getY()) - 20, 40, 40);
        gobang.totalgonumber += 1;
        Difficulty.setVisible(false);
        // 普通PVE
    }

    public void HardPVE() {
        gobang.IsAI = true;
        gobang.thatstart = true;
        gobang.bd = new Board();
        gobang.br = new Brain(gobang.bd, 4, 10);
        gobang.bd.start();
        int[] best = gobang.br.AIMove(Hard);
        gobang.getGolist()[gobang.totalgonumber] = new Piece(8, 8, gobang.totalgonumber % 2 + 1);
        if (gobang.getGolist()[gobang.totalgonumber].getColorPi() != 1)
            gobang.getG().setFill(Color.WHITE);
        else
            gobang.getG().setFill(Color.BLACK);
        gobang.getG().fillOval(gobang.changeX(gobang.getGolist()[gobang.totalgonumber].getX()) - 20,
                gobang.changeY(gobang.getGolist()[gobang.totalgonumber].getY()) - 20, 40, 40);
        gobang.totalgonumber += 1;
        Difficulty.setVisible(false);
        // 困难PVE
    }

    public void BlackWin() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Black Win");
        alert.setHeaderText("分出胜负");
        alert.setContentText("黑棋胜");
        alert.showAndWait();
    }

    public void WhiteWin() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("White Win");
        alert.setHeaderText("分出胜负");
        alert.setContentText("白棋胜");
        alert.showAndWait();
    }


    public void SUBIP() {

        CONTR.gobang.bd = new Board();
        CONTR.gobang.thatstart = true;
        ip = IP.getText();
        GetIP.setVisible(false);
        GetIP.setManaged(false);
        Connecting.setVisible(true);
        Connecting.setManaged(true);
        if (ip.length() == 0) {
            System.out.println("StartServer");
            server = new Server(CONTR.gobang);
            IsConnected = true;
            CONTR.gobang.setConnect(true);
            CONTR.gobang.server = server;
            CONTR.gobang.Type = 1;
            System.out.println("StartServer");
            th = new Thread(server);
            th.start();
            this.EXIT();
        } else {
            client = new Client(ip, CONTR.gobang);
            CONTR.gobang.client = client;

            CONTR.gobang.Type = 2;
            th = new Thread(client);
            th.start();
            if (client.init()) {//判断是否连接成功
                CONTR.ip = this.ip;
                CONTR.gobang.setConnect(true);
                this.EXIT();
            }
            else
            {
                Connecting.setVisible(false);
                Connecting.setManaged(false);
                GetIP.setVisible(true);
                GetIP.setManaged(true);

            }
        }

    }

    public void GetMyIP() throws UnknownHostException {
        InetAddress addr = InetAddress.getByName("127.0.0.1");
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            addr = inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                addr = candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            addr = jdkSuppliedAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        InetAddress addr = InetAddress.getLocalHost();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Your IP");
        alert.setHeaderText("您的IP地址为(不输入则默认为主机)");
        alert.setContentText(addr.toString());
        alert.showAndWait();
    }
}