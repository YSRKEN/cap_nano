/* オプションウィンドウ */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class option_window extends JFrame implements ActionListener{
	/* メンバ変数 */
	// 定数
	static final int object_x = 80;
	static final int object_y = 24;
	static final int object_space = 10;
	// 変数
	public static JCheckBox checkbox1;
	/* コンストラクタ */
	option_window(){
		// ウィンドウの設定
		setTitle("オプション");
		getContentPane().setPreferredSize(new Dimension(position_x(3), position_y(1)));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setAlwaysOnTop(true);
		// オブジェクトの設定
		JPanel panel = new JPanel();
		panel.setLayout(null);
		checkbox1 = new JCheckBox("罫線表示");
			checkbox1.setBounds(position_x(0), position_y(0), size_x(1), size_y(1));
			checkbox1.setMargin(new Insets(0, 0, 0, 0));
			checkbox1.addActionListener(this);
			checkbox1.setActionCommand("罫線表示");
			panel.add(checkbox1);
		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
	}
	/* イベント処理用 */
	public void actionPerformed(ActionEvent event){
		String command_str = event.getActionCommand();
		if(command_str.equals("罫線表示")){
			nano.sort_frame.panel.repaint();
			nano.unit_frame.panel.repaint();
		}
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
}
