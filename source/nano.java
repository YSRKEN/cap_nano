/*
javac nano.java
java nano
jar cvfm nano.jar MANIFEST.MF *.class
jar tf nano.jar
*/

/* とりあえずリリースする
 * 選択中は選択枠を出して分かりやすくする
 * 「挿入枠」を導入。挿入枠の位置に追加する仕様にすることで使いやすくする
 * そもそもソート画面と改装画面がほぼ一緒なので、抽象クラスを定義してそこから継承させる
 */

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class nano{
	public static main_window main_frame;
	public static unit_window unit_frame;
	public static sort_window sort_frame;
	public static void main(String args[]){
		// メイン画面
		main_frame = new main_window();
		main_frame.setVisible(true);
		// 改装一覧用
		unit_frame = new unit_window();
		// ソート一覧用
		sort_frame = new sort_window();
	}
}

/* メインウィンドウの処理 */
class main_window extends JFrame implements ActionListener{
	/* メンバ変数群 */
	static final Event e_event = new Event();
	static JComboBox combo_box = null;
	public static JTextArea text_area;
	// オブジェクト設定用
	static final int object_x = 64;
	static final int object_y = 24;
	static final int object_space = 10;
	static final String soft_name = "記録は大切なの";
	static final String[] save_type = {"通常", "改装", "ソート"};
	/* オブジェクト用 */
	// メイン画面の設定
	main_window(){
		setResizable(false);
		// setPreferredSizeでウィンドウ内部のサイズを調整している
		setTitle(soft_name);
		getContentPane().setPreferredSize(new Dimension(position_x(4), position_y(4)));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// パネルの設定
		JPanel panel = new JPanel();
		panel.setLayout(null);
		// オブジェクトの設定
		JButton button1 = new JButton("座標取得");
			button1.setBounds(position_x(0), position_y(0), size_x(1), size_y(1));
			button1.setMargin(new Insets(0, 0, 0, 0));
			button1.addActionListener(this);
			button1.setActionCommand("座標取得");
		combo_box = new JComboBox(save_type);
			combo_box.setBounds(position_x(1), position_y(0), size_x(1), size_y(1));
			combo_box.addActionListener(this);
			combo_box.setActionCommand("選択変更");
		JButton button2 = new JButton("画像追加");
			button2.setBounds(position_x(2), position_y(0), size_x(1), size_y(1));
			button2.setMargin(new Insets(0, 0, 0, 0));
			button2.addActionListener(this);
			button2.setActionCommand("画像追加");
		JButton button3 = new JButton("画像保存");
			button3.setBounds(position_x(3), position_y(0), size_x(1), size_y(1));
			button3.setMargin(new Insets(0, 0, 0, 0));
			button3.addActionListener(this);
			button3.setActionCommand("画像保存");
		text_area = new JTextArea();
			text_area.setEditable(false);
		JScrollPane scrollpane = new JScrollPane(text_area);
			scrollpane.setBounds(position_x(0), position_y(1), size_x(4), size_y(3));
		// パネルにオブジェクトを追加
		panel.add(button1);
		panel.add(combo_box);
		panel.add(button2);
		panel.add(button3);
		panel.add(scrollpane);
		// コンテナにパネルを追加
		Container content_pane = getContentPane();
		content_pane.add(panel, BorderLayout.CENTER);
		pack();
	}
	// オブジェクト用定数を計算する
	private static int position_x(int x){
		return object_space * (x + 1) + object_x * x;
	}
	private static int position_y(int y){
		return object_space * (y + 1) + object_y * y;
	}
	private static int size_x(int x){
		return object_space * (x - 1) + object_x * x;
	}
	private static int size_y(int y){
		return object_space * (y - 1) + object_y * y;
	}
	// テキストエリアにテキストを追加する
	public static void putLog(String message){
		text_area.append(message + "\n");
	}
	/* イベント処理用 */
	public void actionPerformed(ActionEvent event){
		String command_str = event.getActionCommand();
		if(command_str.equals("座標取得")){
			e_event.getKancollePosition();
		}
		if(command_str.equals("選択変更")){
			switch((String)combo_box.getSelectedItem()){
				case "通常":
					nano.unit_frame.setVisible(false);
					nano.sort_frame.setVisible(false);
					break;
				case "改装":
					nano.unit_frame.setVisible(true);
					nano.sort_frame.setVisible(false);
					break;
				case "ソート":
					nano.unit_frame.setVisible(false);
					nano.sort_frame.setVisible(true);
					break;
			}
		}
		if(command_str.equals("画像追加")){
			switch((String)combo_box.getSelectedItem()){
				case "改装":
					unit_window.addImage(e_event.getImage());
					break;
				case "ソート":
					sort_window.addImage(e_event.getImage());
					break;
			}
		}
		if(command_str.equals("画像保存")){
			switch((String)combo_box.getSelectedItem()){
				case "通常":
					e_event.savePicture();
					break;
				case "改装":
					unit_window.savePicture();
					break;
				case "ソート":
					sort_window.savePicture();
					break;
			}
		}
	}
}

/* 改装一覧の処理 */
class unit_window extends JFrame implements MouseListener{
	/* メンバ変数群 */
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
	static final int unit_pos_x  = 330;
	static final int unit_pos_y  = 100;
	static final int unit_width  = 455;
	static final int unit_height = 365;
	static final int unit_zooming = 4;
	static final int unit_panels_x = 6;
	static final int unit_panels_y = 6;
	static final int unit_window_width  = unit_width  * unit_panels_x;
	static final int unit_window_height = unit_height * unit_panels_y;
	//
	public static BufferedImage unit_list_image = new BufferedImage(unit_window_width, unit_window_height, BufferedImage.TYPE_INT_BGR);
	static ArrayList<Boolean> unit_list_flg = new ArrayList<Boolean>();
	static UnitWindow panel = null;
	static int press_position;
	boolean press_flg = false;
	// オブジェクト設定用
	unit_window(){
		setResizable(false);
		setTitle("改装一覧");
		getContentPane().setPreferredSize(new Dimension(position_x(unit_panels_x), position_y(unit_panels_y)));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panel = new UnitWindow();
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		for(int i = 0; i < unit_panels_x * unit_panels_y; i++){
			unit_list_flg.add(false);
		}
		addMouseListener(this);
		Graphics graphics = unit_list_image.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, unit_window_width, unit_window_height);
	}
	private static int position_x(int x){
		return unit_width * x / unit_zooming;
	}
	private static int position_y(int y){
		return unit_height * y / unit_zooming;
	}
	private static int calcPosition(int x, int y){
		int position = x * unit_panels_y + y;
		if(position < 0) position = 0;
		if(position >= unit_panels_x * unit_panels_y) position = unit_panels_x * unit_panels_y - 1;
		return position;
	}
	/* イベント時用 */
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	// マウスを押した際の処理
	public void mousePressed(MouseEvent event){
		if(press_flg) return;
		Point point = event.getPoint();
		press_position = calcPosition(point.x * unit_zooming / unit_width, point.y * unit_zooming / unit_height);
		press_flg = true;
	}
	// マウスを離した際の処理
	public void mouseReleased(MouseEvent event){
		if(press_flg == false) return;
		Point point = event.getPoint();
		int release_position = calcPosition(point.x * unit_zooming / unit_width, point.y * unit_zooming / unit_height);
		if(press_position != release_position){
			main_window.putLog("【画像交換】");
			main_window.putLog("種別：改装画面");
			main_window.putLog("(" + (press_position / unit_panels_y) + "," + (press_position % unit_panels_y) + ")⇔(" + (release_position / unit_panels_y) + "," + (release_position % unit_panels_y) + ")");
			int x1 =   press_position / unit_panels_y, y1 =   press_position % unit_panels_y;
			int x2 = release_position / unit_panels_y, y2 = release_position % unit_panels_y;
			BufferedImage temp_image1 = unit_list_image.getSubimage(x1 * unit_width, y1 * unit_height, unit_width, unit_height);
			BufferedImage temp_image2 = unit_list_image.getSubimage(x2 * unit_width, y2 * unit_height, unit_width, unit_height);
			BufferedImage unit_list_image_ = new BufferedImage(unit_window_width, unit_window_height, BufferedImage.TYPE_INT_BGR);
			Graphics graphics = unit_list_image_.getGraphics();
			graphics.drawImage(unit_list_image, 0, 0, this);
			graphics.drawImage(temp_image1, x2 * unit_width, y2 * unit_height, this);
			graphics.drawImage(temp_image2, x1 * unit_width, y1 * unit_height, this);
			unit_list_image = unit_list_image_;
			boolean temp = unit_list_flg.get(press_position);
			unit_list_flg.set(press_position, unit_list_flg.get(release_position));
			unit_list_flg.set(release_position, temp);
		}
		press_flg = false;
		panel.repaint();
	}
	public void mouseClicked(MouseEvent event){
		if (event.getClickCount() < 2) return;
		// ダブルクリックした際は、その場所の記録画像を消去する
		Point point = event.getPoint();
		int delete_position = calcPosition(point.x * unit_zooming / unit_width, point.y * unit_zooming / unit_height);
//		if(unit_list_flg.get(delete_position) == false) return;
		unit_list_flg.set(delete_position, false);
		int x = delete_position / unit_panels_y, y = delete_position % unit_panels_y;
		Graphics graphics = unit_list_image.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(x * unit_width, y * unit_height, unit_width, unit_height);
		main_window.putLog("【画像削除】");
		main_window.putLog("種別：改装画面");
		main_window.putLog("追加位置：(" + x+  "," + y + ")");
		panel.repaint();
	}
	static private boolean checkColor(BufferedImage image, int x, int y, int r, int g, int b){
		Color color = new Color(image.getRGB(x, y));
		int diff_r = color.getRed() - r, diff_g = color.getGreen() - g, diff_b = color.getBlue() - b;
		int diff = diff_r * diff_r + diff_g * diff_g + diff_b * diff_b;
		if(diff < 500) return true;
		return false;
	}
	static public void addImage(BufferedImage image){
		// 事前に、改装画面なのかを検査する
		if(checkColor(image, 300, 172, 241, 191, 119) == false) return;
		// 追加処理
		for(int j = 0; j < 2; j++){
			for(int i = 0; i < 3; i++){
				for(int n = 0; n < 3; n++){
					for(int m = 0; m < 2; m++){
						int x = i * 2 + m, y = j * 3 + n;
						//System.out.println(unit_list_flg.get(x * unit_panels_y + y));
						if(unit_list_flg.get(x * unit_panels_y + y)) continue;
						// 空き場所を探した上で、その位置に画像を追加する
						main_window.putLog("【画像追加】");
						main_window.putLog("種別：改装画面");
						main_window.putLog("追加位置：(" + x+  "," + y + ")");
						Graphics graphics = unit_list_image.getGraphics();
						graphics.drawImage(image.getSubimage(unit_pos_x, unit_pos_y, unit_width, unit_height), x * unit_width, y * unit_height, null);
						unit_list_flg.set(x * unit_panels_y + y, true);
						panel.repaint();
						return;
					}
				}
			}
		}
	}
	static public void savePicture(){
		// 保存画像の領域を決定する
		int px1 = 0, py1 = 0, px2 = 0, py2 = 0;
		boolean flg = false;
		for(int x = 0; x < unit_panels_x; x++){
			for(int y = 0; y < unit_panels_y; y++){
				if(unit_list_flg.get(x * unit_panels_y + y)){
					if(flg == false){
						px1 = px2 = x;
						py1 = py2 = y;
						flg = true;
					}else{
						px1 = Math.min(px1, x);
						py1 = Math.min(py1, y);
						px2 = Math.max(px2, x);
						py2 = Math.max(py2, y);
					}
				}
			}
		}
		if(flg == false) return;
		main_window.putLog("【画像保存(改装)】");
		String save_name = sdf.format(Calendar.getInstance().getTime()) + ".png";
		main_window.putLog(save_name);
		try{
			ImageIO.write(unit_list_image.getSubimage(px1 * unit_width, py1 * unit_height, (px2 - px1 + 1) * unit_width, (py2 - py1 + 1) * unit_height), "png", new File(save_name));
		}
		catch(Exception error){
			error.printStackTrace();
		}
	}
	class UnitWindow extends JPanel{
		public void paintComponent(Graphics graphics){
			graphics.drawImage(unit_list_image.getScaledInstance(position_x(unit_panels_x), position_y(unit_panels_y), Image.SCALE_AREA_AVERAGING), 0, 0, this);
			Graphics2D graphics2d = (Graphics2D)graphics;
			for(int x = 1; x <= unit_panels_x - 1; x++){
				graphics2d.draw(new Line2D.Double(position_x(x), position_y(0), position_x(x), position_y(unit_panels_y)));
			}
			for(int y = 1; y <= unit_panels_y - 1; y++){
				graphics2d.draw(new Line2D.Double(position_x(0), position_y(y), position_x(unit_panels_x), position_y(y)));
			}
		}
	}
}

/* ソート一覧の処理 */
class sort_window extends JFrame implements MouseListener{
	/* メンバ変数群 */
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
	static final int sort_pos_x  = 398;
	static final int sort_pos_y  = 154;
	static final int sort_width  = 194;
	static final int sort_height = 279;
	static final int sort_zooming =  3;
	static final int sort_panels_x = 7;
	static final int sort_panels_y = 5;
	static final int sort_window_width  = sort_width  * sort_panels_x;
	static final int sort_window_height = sort_height * sort_panels_y;
	//
	public static BufferedImage sort_list_image = new BufferedImage(sort_window_width, sort_window_height, BufferedImage.TYPE_INT_BGR);
	static ArrayList<Boolean> sort_list_flg = new ArrayList<Boolean>();
	static sortWindow panel = null;
	static int press_position;
	boolean press_flg = false;
	// オブジェクト設定用
	sort_window(){
		setResizable(false);
		setTitle("ソート一覧");
		getContentPane().setPreferredSize(new Dimension(position_x(sort_panels_x), position_y(sort_panels_y)));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panel = new sortWindow();
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		for(int i = 0; i < sort_panels_x * sort_panels_y; i++){
			sort_list_flg.add(false);
		}
		addMouseListener(this);
		Graphics graphics = sort_list_image.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, sort_window_width, sort_window_height);
	}
	private static int position_x(int x){
		return sort_width * x / sort_zooming;
	}
	private static int position_y(int y){
		return sort_height * y / sort_zooming;
	}
	private static int calcPosition(int x, int y){
		int position = x * sort_panels_y + y;
		if(position < 0) position = 0;
		if(position >= sort_panels_x * sort_panels_y) position = sort_panels_x * sort_panels_y - 1;
		return position;
	}
	/* イベント時用 */
	public void mouseEntered(MouseEvent event){}
	public void mouseExited(MouseEvent event){}
	// マウスを押した際の処理
	public void mousePressed(MouseEvent event){
		if(press_flg) return;
		Point point = event.getPoint();
		press_position = calcPosition(point.x * sort_zooming / sort_width, point.y * sort_zooming / sort_height);
		press_flg = true;
	}
	// マウスを離した際の処理
	public void mouseReleased(MouseEvent event){
		if(press_flg == false) return;
		Point point = event.getPoint();
		int release_position = calcPosition(point.x * sort_zooming / sort_width, point.y * sort_zooming / sort_height);
		if(press_position != release_position){
			main_window.putLog("【画像交換】");
			main_window.putLog("種別：ソート画面");
			main_window.putLog("(" + (press_position / sort_panels_y) + "," + (press_position % sort_panels_y) + ")⇔(" + (release_position / sort_panels_y) + "," + (release_position % sort_panels_y) + ")");
			int x1 =   press_position / sort_panels_y, y1 =   press_position % sort_panels_y;
			int x2 = release_position / sort_panels_y, y2 = release_position % sort_panels_y;
			BufferedImage temp_image1 = sort_list_image.getSubimage(x1 * sort_width, y1 * sort_height, sort_width, sort_height);
			BufferedImage temp_image2 = sort_list_image.getSubimage(x2 * sort_width, y2 * sort_height, sort_width, sort_height);
			BufferedImage sort_list_image_ = new BufferedImage(sort_window_width, sort_window_height, BufferedImage.TYPE_INT_BGR);
			Graphics graphics = sort_list_image_.getGraphics();
			graphics.drawImage(sort_list_image, 0, 0, this);
			graphics.drawImage(temp_image1, x2 * sort_width, y2 * sort_height, this);
			graphics.drawImage(temp_image2, x1 * sort_width, y1 * sort_height, this);
			sort_list_image = sort_list_image_;
			boolean temp = sort_list_flg.get(press_position);
			sort_list_flg.set(press_position, sort_list_flg.get(release_position));
			sort_list_flg.set(release_position, temp);
		}
		press_flg = false;
		panel.repaint();
	}
	public void mouseClicked(MouseEvent event){
		if (event.getClickCount() < 2) return;
		// ダブルクリックした際は、その場所の記録画像を消去する
		Point point = event.getPoint();
		int delete_position = calcPosition(point.x * sort_zooming / sort_width, point.y * sort_zooming / sort_height);
		if(sort_list_flg.get(delete_position) == false) return;
		sort_list_flg.set(delete_position, false);
		int x = delete_position / sort_panels_y, y = delete_position % sort_panels_y;
		Graphics graphics = sort_list_image.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(x * sort_width, y * sort_height, sort_width, sort_height);
		main_window.putLog("【画像削除】");
		main_window.putLog("種別：ソート画面");
		main_window.putLog("追加位置：(" + x+  "," + y + ")");
		panel.repaint();
	}
	static private boolean checkColor(BufferedImage image, int x, int y, int r, int g, int b){
		Color color = new Color(image.getRGB(x, y));
		int diff_r = color.getRed() - r, diff_g = color.getGreen() - g, diff_b = color.getBlue() - b;
		int diff = diff_r * diff_r + diff_g * diff_g + diff_b * diff_b;
		if(diff < 500) return true;
		return false;
	}
	static public void addImage(BufferedImage image){
		// 事前に、ソート画面なのかを検査する
		if(checkColor(image, 420, 118, 66,  60,  59) == false) return;
		if(checkColor(image, 374,  80, 30, 157, 160) == false) return;
		// 追加処理
		for(int x = 0; x < sort_panels_x; x++){
			for(int y = 0; y < sort_panels_y; y++){
				if(sort_list_flg.get(x * sort_panels_y + y)) continue;
				// 空き場所を探した上で、その位置に画像を追加する
				main_window.putLog("【画像追加】");
				main_window.putLog("種別：ソート画面");
				main_window.putLog("追加位置：(" + x+  "," + y + ")");
				Graphics graphics = sort_list_image.getGraphics();
				graphics.drawImage(image.getSubimage(sort_pos_x, sort_pos_y, sort_width, sort_height), x * sort_width, y * sort_height, null);
				sort_list_flg.set(x * sort_panels_y + y, true);
				panel.repaint();
				return;
			}
		}
	}
	static public void savePicture(){
		// 保存画像の領域を決定する
		int px1 = 0, py1 = 0, px2 = 0, py2 = 0;
		boolean flg = false;
		for(int x = 0; x < sort_panels_x; x++){
			for(int y = 0; y < sort_panels_y; y++){
				if(sort_list_flg.get(x * sort_panels_y + y)){
					if(flg == false){
						px1 = px2 = x;
						py1 = py2 = y;
						flg = true;
					}else{
						px1 = Math.min(px1, x);
						py1 = Math.min(py1, y);
						px2 = Math.max(px2, x);
						py2 = Math.max(py2, y);
					}
				}
			}
		}
		if(flg == false) return;
		main_window.putLog("【画像保存(ソート)】");
		String save_name = sdf.format(Calendar.getInstance().getTime()) + ".png";
		main_window.putLog(save_name);
		try{
			ImageIO.write(sort_list_image.getSubimage(px1 * sort_width, py1 * sort_height, (px2 - px1 + 1) * sort_width, (py2 - py1 + 1) * sort_height), "png", new File(save_name));
		}
		catch(Exception error){
			error.printStackTrace();
		}
	}
	class sortWindow extends JPanel{
		public void paintComponent(Graphics graphics){
			graphics.drawImage(sort_list_image.getScaledInstance(position_x(sort_panels_x), position_y(sort_panels_y), Image.SCALE_AREA_AVERAGING), 0, 0, this);
			Graphics2D graphics2d = (Graphics2D)graphics;
			for(int x = 1; x <= sort_panels_x - 1; x++){
				graphics2d.draw(new Line2D.Double(position_x(x), position_y(0), position_x(x), position_y(sort_panels_y)));
			}
			for(int y = 1; y <= sort_panels_y - 1; y++){
				graphics2d.draw(new Line2D.Double(position_x(0), position_y(y), position_x(sort_panels_x), position_y(y)));
			}
		}
	}
}

/* イベント時の処理 */
class Event{
	/* メンバ変数群 */
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
	// 画像認識用
	static final int flash_width = 800;
	static final int flash_height = 480;
	static final int judge_x = flash_width + 2;
	static final int judge_y = flash_height + 2;
	static ArrayList<Integer> gd_index;
	static ArrayList<Integer> gc_index;
	static int display_index = -1, flash_pos_x, flash_pos_y;
	// 艦これの座標を取得する
	static public void getKancollePosition(){
		display_index = -1;
		main_window.putLog("【座標取得】");
		// 座標を取得する処理
		// まず、全てのディスプレイにおけるスクショを取得する
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		gd_index = new ArrayList<Integer>();
		gc_index = new ArrayList<Integer>();
		try{
			// 全てのグラフィックスデバイスに関する情報を取得する
			GraphicsDevice[] all_gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
			int i = 0, j;
			for(GraphicsDevice gd : all_gd){
				// 各グラフィックデバイスにおけるグラフィックス特性を取得する
				GraphicsConfiguration[] all_gc = gd.getConfigurations();
				// 各グラフィックス特性に従い、その座標を取得してスクショを撮る
				Robot robot = new Robot(gd);
				j = 0;
				for(GraphicsConfiguration gc : all_gc){
					images.add(robot.createScreenCapture(gc.getBounds()));
					gd_index.add(i);
					gc_index.add(j);
					j++;
				}
				i++;
			}
			// 各スクショにおいて、艦これの画面となりうる800x480の画像を探索する
			for(int n = images.size() - 1; n >= 0; n--){
				int width  = images.get(n).getWidth();
				int height = images.get(n).getHeight();
				// 艦これの画面が存在しえないほどのスクショサイズなら探索しない
				if((width < judge_x) || (height < judge_y)) continue;
				// 艦これの画面を検索する
				int search_x = width  - judge_x;
				int search_y = height - judge_y;
				for(int x = 0; x <= search_x; x++){
					for(int y = 0; y <= search_y; y++){
						// 「左上」
						//[0xFFFFFF]0xFFFFFF
						// 0xFFFFFF 0x??????
						// ↑まず参照するのは[]の位置。上記パターンを見つけてから他の3角を見つける
						if((images.get(n).getRGB(x    , y    ) & 0xffffff) != 0xffffff) continue;
						if((images.get(n).getRGB(x + 1, y    ) & 0xffffff) != 0xffffff) continue;
						if((images.get(n).getRGB(x    , y + 1) & 0xffffff) != 0xffffff) continue;
						if((images.get(n).getRGB(x + 1, y + 1) & 0xffffff) == 0xffffff) continue;
						// 「右上」
						if((images.get(n).getRGB(x + flash_width    , y    ) & 0xffffff) != 0xffffff) continue;
						if((images.get(n).getRGB(x + flash_width + 1, y    ) & 0xffffff) != 0xffffff) continue;
						if((images.get(n).getRGB(x + flash_width    , y + 1) & 0xffffff) == 0xffffff) continue;
						if((images.get(n).getRGB(x + flash_width + 1, y + 1) & 0xffffff) != 0xffffff) continue;
						// 「左下」
						if((images.get(n).getRGB(x    , y + flash_height    ) & 0xffffff) != 0xffffff) continue;
						if((images.get(n).getRGB(x + 1, y + flash_height    ) & 0xffffff) == 0xffffff) continue;
						if((images.get(n).getRGB(x    , y + flash_height + 1) & 0xffffff) != 0xffffff) continue;
						if((images.get(n).getRGB(x + 1, y + flash_height + 1) & 0xffffff) != 0xffffff) continue;
						// 「右下」
						if((images.get(n).getRGB(x + flash_width    , y + flash_height    ) & 0xffffff) == 0xffffff) continue;
						if((images.get(n).getRGB(x + flash_width + 1, y + flash_height    ) & 0xffffff) != 0xffffff) continue;
						if((images.get(n).getRGB(x + flash_width    , y + flash_height + 1) & 0xffffff) != 0xffffff) continue;
						if((images.get(n).getRGB(x + flash_width + 1, y + flash_height + 1) & 0xffffff) != 0xffffff) continue;
						// 検出できたので、そのディスプレイの番号および座標を取得する
						display_index = n;
						flash_pos_x = x + 1;
						flash_pos_y = y + 1;
						break;
					}
					if(display_index >= 0) break;
				}
			}
			if(display_index >= 0){
				main_window.putLog("ディスプレイ番号-左上座標：" + display_index + "-" + flash_pos_x + "," + flash_pos_y);
			}else{
				main_window.putLog("艦これの画面を取得できませんでした。");
			}
		}
		catch(Exception error){
			error.printStackTrace();
		}
	}
	// 艦これの画像を保存する
	static public void savePicture(){
		try{
			if(display_index < 0) return;
			// 画像を保存する処理
			main_window.putLog("【画像保存】");
			BufferedImage latest_flash_image = getImage();
			Date latest_flash_time = getDate();
			String save_name = sdf.format(latest_flash_time) + ".png";
			main_window.putLog(save_name);
			ImageIO.write(latest_flash_image, "png", new File(save_name));
		}
		catch(Exception error){
			error.printStackTrace();
		}
	}
	// 艦これの画面を取得する
	static public BufferedImage getImage(){
		try{
			if(display_index < 0) return null;
			GraphicsDevice[] all_gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
			GraphicsConfiguration[] all_gc = all_gd[gd_index.get(display_index)].getConfigurations();
			Robot robot = new Robot(all_gd[gd_index.get(display_index)]);
			return robot.createScreenCapture(all_gc[gc_index.get(display_index)].getBounds()).getSubimage(flash_pos_x, flash_pos_y, flash_width, flash_height);
		}
		catch(Exception error){
			error.printStackTrace();
			return null;
		}
	}
	// 現在時刻を取得する
	static public Date getDate(){
		return Calendar.getInstance().getTime();
	}
}
