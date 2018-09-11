package application;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import com.AWork.Piece.Class.Piece;
import com.TeamWork.Data.AI_Info.BaseBoard_Data;
import com.TeamWork.Data.Global.Data;
import com.AWork.Fuction.AI.AIBoard.Board;
import com.AWork.Fuction.AI.AIBrain.Brain;
import com.AWork.Fuction.IsWin.IsWin;

public class GoBang implements Data, BaseBoard_Data {
    public int x;
    public int y;

    private boolean IsConnect = false;
    public int Type = 0;
    private double temX = -1;
    private double temY = -1;
    public boolean judge = true;
    public int totalgonumber = 0;
    public boolean thatstart = false;
    public boolean IsAI = false;
    Piece[][] boardPi = new Piece[BT][BT];// 11
    int[][] piece = new int[RC][RC];// 00
    Piece[] golist = new Piece[250];
    static coordinate[][] pos = new coordinate[15][15];
    Canvas c = new Canvas(891, 846);
    GraphicsContext g = c.getGraphicsContext2D();

    public Board bd;
    public Brain br;

    public Server server;
    public Client client;

    private boolean putChess(int d, int e) {
        if (bd.putChess(d, e)) {

            return true;
        }
        return false;
    }

    public int changeX(int x) {
        return (x - 1) * 50 + 150;
    }

    public int changeY(int x) {
        return (x - 1) * 50 + 75;
    }

    public void DrawPan() {
        g.save();
        g.setFill(Color.BURLYWOOD);
        g.fillRect(91, 50, 800, 750);

        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                if ((i + 1) % 2 != 0 && (j + 1) % 2 != 0)
                    g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                else if ((i + 1) % 2 == 0 && (j + 1) % 2 == 0)
                    g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
                else
                    g.strokeRect(150 + i * 50, 75 + j * 50, 50, 50);
            }
        }
    }

    GoBang() {
            DrawPan();
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    pos[i][j] = new coordinate(150 + i * 50.0, 75 + j * 50.0);
                }
            }
            g.restore();

        c.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        if ((event.getX() - pos[i][j].x) * (event.getX() - pos[i][j].x)
                                + (event.getY() - pos[i][j].y) * (event.getY() - pos[i][j].y) < 625) {
                            temX = pos[i][j].x;
                            temY = pos[i][j].y;
                            x = i;
                            y = j;

                        }
                    }
                }
                if (Type == 1)
                    server.setLocation(x, y);
                if (Type == 2)
                    client.setLocation(x, y);
                AddPiece(x, y);
            }

        });

    }

    public void AddPiece(int x, int y) {
        for (int i = 0; i < totalgonumber; i++) {
            if (temX == changeX(golist[i].getX()) && temY == changeY(golist[i].getY()))
                judge = false;
            else {
                judge = true;
            }
        }

        if (judge && temY > 0 && temX > 0 && thatstart && putChess(x + 1, y + 1)) {
            golist[totalgonumber] = new Piece(x + 1, y + 1, totalgonumber % 2 + 1);

            piece[x][y] = totalgonumber % 2 + 1;
            if (golist[totalgonumber].getColorPi() == whitePiece)
                g.setFill(Color.WHITE);
            else
                g.setFill(Color.BLACK);
            g.fillOval(temX - 20, temY - 20, 40, 40);
            totalgonumber += 1;
            if (IsAI) {
                int[] best = br.AIMove(Hard);
                putChess(best[0], best[1]);
                golist[totalgonumber] = new Piece(best[0], best[1], totalgonumber % 2 + 1);
                piece[best[0] - 1][best[1] - 1] = totalgonumber % 2 + 1;
                if (golist[totalgonumber].getColorPi() == 2)
                    g.setFill(Color.WHITE);
                else
                    g.setFill(Color.BLACK);
                g.fillOval(changeX(golist[totalgonumber].getX()) - 20, changeY(golist[totalgonumber].getY()) - 20, 40,
                        40);
                totalgonumber += 1;
            }

        }

//        for (int i = 0; i < RC; i++) {
//            for (int j = 0; j < RC; j++)
//                System.out.print(piece[j][i] + " ");
//            System.out.println();
//        }

        // changData();

        if (IsAI) {
            if (bd.isGameOver() == whitePiece || bd.isGameOver() == blackPiece) {
                if (totalgonumber % 2 == 0) {
                    // °×
                    System.out.println("1111");
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("White Win");
                    alert.setHeaderText("·Ö³öÊ¤¸º");
                    alert.setContentText("°×ÆåÊ¤");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                } else {
                    // ºÚ
                    System.out.println("1111");
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Black Win");
                    alert.setHeaderText("·Ö³öÊ¤¸º");
                    alert.setContentText("ºÚÆåÊ¤");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                }
                bd.reset();

            }
        } else {
//            for (int i = 0; i < RC; i++) {
//                for (int j = 0; j < RC; j++)
//                    System.out.print(piece[i][j] + " ");
//                System.out.println();
//            }
            if (IsWin.Checkborad(5, piece)) {
//                System.out.println("12234234");
                if (totalgonumber % 2 == 0) {
                    // °×
                    System.out.println("1111");
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("White Win");
                    alert.setHeaderText("·Ö³öÊ¤¸º");
                    alert.setContentText("°×ÆåÊ¤");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                    bd.reset();
                } else {
                    // ºÚ
                    System.out.println("1111");
                    thatstart = false;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Black Win");
                    alert.setHeaderText("·Ö³öÊ¤¸º");
                    alert.setContentText("ºÚÆåÊ¤");
                    alert.showAndWait();
                    DrawPan();
                    clearlist();
                    totalgonumber = 0;
                    bd.reset();
                }

            }
        }
    }

    public void changData() {
        boardPi = bd.getData();
        for (int i = 1; i < RC; i++)
            for (int j = 1; j < RC; j++)
                piece[i - 1][j - 1] = boardPi[i][j].getColorPi();

    }

    public void clearlist() {
        for (int i = 1; i < RC; i++)
            for (int j = 1; j < RC; j++)
                piece[i][j] = emptyPiece;
        for (int i = 0; i < totalgonumber; i++) {
            golist[i].setX(-1);
            golist[i].setY(-1);
            golist[i].setColorPi(emptyPiece);
        }
    }

    public boolean isConnect() {
        return IsConnect;
    }

    public void setConnect(boolean connect) {
        IsConnect = connect;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public class coordinate {
        public double x;
        public double y;
        public coordinate(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

}
