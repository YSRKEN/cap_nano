/* メインウィンドウ */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import jdk.nashorn.internal.ir.BreakableNode;

public class main_window extends JFrame implements ActionListener{
	/* メンバ変数 */
	// 定数
	static final int object_x = 80;
	static final int object_y = 24;
	static final int object_space = 10;
	static final String soft_name = "記録は大切なの";
	static final String[] mode_type_str = {"通常", "改装", "ソート"};
	static final String[] save_type_str = {"通常", "編成", "資材"};
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
	// 変数
	static int save_type = 0;
	public static JComboBox combo_box1;
	static JComboBox combo_box2, join_combo_dir, join_combo_type;
	static DefaultComboBoxModel model_save, model_dir, model_type;
	public static JTextArea text_area;
	public static Timer timer;
	/* コンストラクタ */
	main_window(){
		// ウィンドウの設定
		setTitle(soft_name);
		getContentPane().setPreferredSize(new Dimension(position_x(4), position_y(5)));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setAlwaysOnTop(true);
		// オブジェクトの設定
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JButton button1 = new JButton("座標取得");
			button1.setBounds(position_x(0), position_y(0), size_x(1), size_y(1));
			button1.setMargin(new Insets(0, 0, 0, 0));
			button1.addActionListener(this);
			button1.setActionCommand("座標取得");
			panel.add(button1);
		combo_box1 = new JComboBox(mode_type_str);
			combo_box1.setBounds(position_x(1), position_y(0), size_x(1), size_y(1));
			combo_box1.addActionListener(this);
			combo_box1.setActionCommand("モード変更");
			panel.add(combo_box1);
		JButton button2 = new JButton("画像追加");
			button2.setBounds(position_x(2), position_y(0), size_x(1), size_y(1));
			button2.setMargin(new Insets(0, 0, 0, 0));
			button2.addActionListener(this);
			button2.setActionCommand("画像追加");
			button2.setMnemonic(KeyEvent.VK_Z);
			panel.add(button2);
		JButton button3 = new JButton("画像保存");
			button3.setBounds(position_x(3), position_y(0), size_x(1), size_y(1));
			button3.setMargin(new Insets(0, 0, 0, 0));
			button3.addActionListener(this);
			button3.setActionCommand("画像保存");
			panel.add(button3);
		text_area = new JTextArea();
			text_area.setEditable(false);
			JScrollPane scrollpane = new JScrollPane(text_area);
			scrollpane.setBounds(position_x(0), position_y(2), size_x(4), size_y(3));
			panel.add(scrollpane);
		model_save = new DefaultComboBoxModel(save_type_str);
			combo_box2 = new JComboBox(model_save);
			combo_box2.setBounds(position_x(0), position_y(1), size_x(1), size_y(1));
			combo_box2.addActionListener(this);
			combo_box2.setActionCommand("保存種別");
			panel.add(combo_box2);
		model_dir = new DefaultComboBoxModel();
			join_combo_dir = new JComboBox(model_dir);
			join_combo_dir.setBounds(position_x(1), position_y(1), size_x(1), size_y(1));
			join_combo_dir.addActionListener(this);
			join_combo_dir.setActionCommand("方向変更");
			join_combo_dir.setEnabled(false);
			panel.add(join_combo_dir);
		model_type = new DefaultComboBoxModel();
			join_combo_type = new JComboBox(model_type);
			join_combo_type.setBounds(position_x(2), position_y(1), size_x(1), size_y(1));
			join_combo_type.addActionListener(this);
			join_combo_type.setActionCommand("種類変更");
			join_combo_type.setEnabled(false);
			panel.add(join_combo_type);
		JButton button4 = new JButton("オプション");
			button4.setBounds(position_x(3), position_y(1), size_x(1), size_y(1));
			button4.setMargin(new Insets(0, 0, 0, 0));
			button4.addActionListener(this);
			button4.setActionCommand("オプション");
			panel.add(button4);
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		timer = new Timer(500, this);
		timer.start();
	}
	/* イベント処理用 */
	public void actionPerformed(ActionEvent event){
		// メインウィンドウの入力に対する処理
		String command_str = event.getActionCommand(), mode_str = (String)combo_box1.getSelectedItem();
//		System.out.println(command_str + " " + mode_str);
		if(command_str == null){
			// タイマーイベントの処理
			if(option_window.checkbox2.isSelected()){
				switch(mode_str){
				case "改装":
					nano.unit_frame.addImageX(capture.getImage());
					break;
				}
			}
			if(option_window.fps != 0){
				switch(mode_str){
				case "通常":
					savePicture();
					break;
				}
			}
			return;
		}
		switch(command_str){
		case "座標取得":
			capture.getKancollePosition();
			break;
		case "モード変更":
			switch(mode_str){
			case "通常":
				nano.unit_frame.setVisible(false);
				nano.sort_frame.setVisible(false);
				model_save = new DefaultComboBoxModel(save_type_str);
				model_dir  = new DefaultComboBoxModel();
				model_type = new DefaultComboBoxModel();
				model_save.setSelectedItem(model_save.getElementAt(save_type));
				combo_box2.setModel(model_save);
				join_combo_dir.setModel(model_dir);
				join_combo_type.setModel(model_type);
				combo_box2.setEnabled(true);
				join_combo_dir.setEnabled(false);
				join_combo_type.setEnabled(false);
				if(option_window.fps != 0){
					main_window.timer.restart();
					timer.setDelay(1000 / option_window.fps);
				}else{
					timer.setDelay(1000 * 60 * 60 * 24);
				}
				break;
			case "改装":
				nano.unit_frame.setVisible(true);
				nano.sort_frame.setVisible(false);
				model_save = new DefaultComboBoxModel();
				model_dir  = new DefaultComboBoxModel(nano.unit_frame.show_dir_str);
				model_type = new DefaultComboBoxModel(nano.unit_frame.show_type_str);
				model_dir.setSelectedItem(model_dir.getElementAt(nano.unit_frame.show_dir));
				model_type.setSelectedItem(model_type.getElementAt(nano.unit_frame.show_type));
				combo_box2.setModel(model_save);
				join_combo_dir.setModel(model_dir);
				join_combo_type.setModel(model_type);
				combo_box2.setEnabled(false);
				join_combo_dir.setEnabled(true);
				join_combo_type.setEnabled(true);
				if(option_window.checkbox2.isSelected()){
					main_window.timer.restart();
					timer.setDelay(500);
				}else{
					timer.setDelay(1000 * 60 * 60 * 24);
				}
				break;
			case "ソート":
				nano.unit_frame.setVisible(false);
				nano.sort_frame.setVisible(true);
				model_save = new DefaultComboBoxModel();
				model_dir  = new DefaultComboBoxModel(nano.sort_frame.show_dir_str);
				model_type = new DefaultComboBoxModel(nano.sort_frame.show_type_str);
				model_dir.setSelectedItem(model_dir.getElementAt(nano.sort_frame.show_dir));
				model_type.setSelectedItem(model_type.getElementAt(nano.sort_frame.show_type));
				combo_box2.setModel(model_save);
				join_combo_dir.setModel(model_dir);
				join_combo_type.setModel(model_type);
				combo_box2.setEnabled(false);
				join_combo_dir.setEnabled(true);
				join_combo_type.setEnabled(true);
				timer.setDelay(1000 * 60 * 60 * 24);
				break;
			}
			break;
		case "方向変更":
			switch(mode_str){
			case "改装":
				nano.unit_frame.changeMode(join_combo_dir.getSelectedIndex(), join_combo_type.getSelectedIndex());
				break;
			case "ソート":
				nano.sort_frame.changeMode(join_combo_dir.getSelectedIndex(), join_combo_type.getSelectedIndex());
				break;
			}
			break;
		case "種類変更":
			switch(mode_str){
			case "改装":
				nano.unit_frame.changeMode(join_combo_dir.getSelectedIndex(), join_combo_type.getSelectedIndex());
				break;
			case "ソート":
				nano.sort_frame.changeMode(join_combo_dir.getSelectedIndex(), join_combo_type.getSelectedIndex());
				break;
			}
			break;
		case "画像追加":
			switch(mode_str){
			case "改装":
				nano.unit_frame.addImage(capture.getImage());
				break;
			case "ソート":
				nano.sort_frame.addImage(capture.getImage());
				break;
			}
			break;
		case "画像保存":
			switch(mode_str){
			case "通常":
				savePicture();
				break;
			case "改装":
				nano.unit_frame.savePicture();
				break;
			case "ソート":
				nano.sort_frame.savePicture();
				break;
			}
			break;
		case "オプション":
			nano.option_frame.setVisible(true);
			break;
		}
	}
	/* テキストエリアにテキストを追加する */
	public static void putLog(String message){
		text_area.append(message + "\n");
	}
	/* オブジェクト用定数を計算する */
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
	/* 名前隠し機能 */
	private static void disableName(BufferedImage image){
		Graphics graphics = image.getGraphics();
		// 母港左上の提督名
		if(checkHome(image)){
			graphics.setColor(new Color(38, 38, 38));
			graphics.fillRect(111, 0, 162, 25);
		}
		// 艦隊司令部情報
		if(join_window.checkColor(image, 306,276,84,84,84)){
			if(join_window.checkColor(image, 251,203,35,158,159)){
				if(join_window.checkColor(image, 272,479,159,155,61)){
					graphics.setColor(new Color(241, 234, 221));
					graphics.fillRect(201, 123, 295, 30);
				}
			}
		}
		// ランキング
		if(join_window.checkColor(image, 87, 189, 79, 152, 139)){
			if(join_window.checkColor(image, 158, 81, 196, 169, 87)){
				if(join_window.checkColor(image, 47, 333, 115, 166, 202)){
					graphics.setColor(new Color(54, 54, 54));
					graphics.fillRect(225, 153, 150, 298);
				}
			}
		}
		// 演習一覧
		if(join_window.checkColor(image, 140, 131, 103, 83, 46)){
			if(join_window.checkColor(image, 654,119,255,191,96)){
				if(join_window.checkColor(image, 654,119,255,191,96)){
					graphics.setColor(new Color(225, 209, 181));
					graphics.fillRect(338, 178, 165, 14);
					graphics.setColor(new Color(237, 223, 207));
					graphics.fillRect(338, 233, 165, 14);
					graphics.setColor(new Color(225, 209, 181));
					graphics.fillRect(338, 288, 165, 14);
					graphics.setColor(new Color(237, 223, 207));
					graphics.fillRect(338, 343, 165, 14);
					graphics.setColor(new Color(225, 209, 181));
					graphics.fillRect(338, 398, 165, 14);
				}
			}
		}
		// 演習個別
		if(join_window.checkColor(image, 0,0,0,0,0)){
			if(join_window.checkColor(image, 168,165,17,156,160)){
				if(join_window.checkColor(image, 635,444,224,217,204)){
					graphics.setColor(new Color(246, 239, 228));
					graphics.fillRect(130, 87, 295, 30);
				}
			}
		}
		// 戦果報告
		if(join_window.checkColor(image, 51,77,255,246,242)){
			if(join_window.checkColor(image, 395,289,255,246,242)){
				if(join_window.checkColor(image, 0,0,36,54,63)){
					graphics.setColor(new Color(37, 44, 47));
					graphics.fillRect(56, 82, 172, 24);
				}
			}
		}
		graphics.dispose();
	}
	private static boolean checkHome(BufferedImage image){
		if(!join_window.checkColor(image, 665, 42, 83, 159, 73)) return false;
		return join_window.checkColor(image, 736, 61, 172, 128, 95);
	}
	/* 画像保存 */
	private static void savePicture(){
		try{
			if(capture.display_index < 0) return;
			BufferedImage flash_image = capture.getImage();
			if(flash_image == null) return;
			String save_name = sdf.format(Calendar.getInstance().getTime()) + ".png";
			switch((String)combo_box2.getSelectedItem()){
			case "通常":
				if(option_window.checkbox3.isSelected()) disableName(flash_image);
				putLog("【画像保存】");
				ImageIO.write(flash_image, "png", new File(save_name));
				break;
			case "編成":
				if(!join_window.checkColor(flash_image, 420,118,195,180,169)) return;
				if(!join_window.checkColor(flash_image, 506,114,115,180,191)) return;
				putLog("【画像保存】");
				ImageIO.write(flash_image.getSubimage(100, 94, 700, 386), "png", new File(save_name));
				break;
			case "資材":
				BufferedImage supply_image = new BufferedImage(229, 60, BufferedImage.TYPE_INT_BGR);
				Graphics graphics = supply_image.getGraphics();
				graphics.drawImage(flash_image.getSubimage(9, 407, 86, 60), 0, 0, null);	//時刻
				graphics.drawImage(flash_image.getSubimage(657, 9, 143, 60), 86, 0, null);	//資材
				graphics.dispose();
				putLog("【画像保存】");
				ImageIO.write(supply_image, "png", new File(save_name));
				break;
			}
			putLog(save_name);
		}
		catch(Exception error){
			error.printStackTrace();
		}
	}
}
