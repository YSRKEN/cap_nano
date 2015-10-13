/* 改装一覧ウィンドウ */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;

public class unit_window extends join_window{
	/* メンバ変数 */
	// 定数
	static final int[] position_x = {327, 327};
	static final int[] position_y = {103, 103};
	static final int[] block_size_x = {230, 460};
	static final int[] block_size_y = {365, 365};
	static final int blocks_x = 6;
	static final int blocks_y = 6;
	static final int zooming = 4;
	public static final String[] show_dir_str  = {"行を優先", "列を優先", "艦隊優先"};
	public static final String[] show_type_str = {"縮小表示", "通常表示"};
	static final String title = "改装一覧";
	static final BasicStroke wideStroke = new BasicStroke(8.0f);
	// 変数
	static int show_dir  = 0;	//表示方向(行優先・列優先)
	static int show_type = 1;	//表示種類(コンパクト・通常)
	static int[] index = new int[blocks_x * blocks_y];
	/* コンストラクタ */
	unit_window(){
		super();
		int count = 0;
		for(int j = 0; j < 2; j++){
			for(int i = 0; i < 3; i++){
				for(int n = 0; n < 3; n++){
					for(int m = 0; m < 2; m++){
						int x = i * 2 + m, y = j * 3 + n;
						index[y * blocks_x + x] = count;
						count++;
					}
				}
			}
		}
	}
	/* アクセッサ */
	int get_position_x(){return position_x[show_type];}
	int get_position_y(){return position_y[show_type];}
	int get_block_size_x(){return block_size_x[show_type];}
	int get_block_size_y(){return block_size_y[show_type];}
	int get_blocks_x(){return blocks_x;}
	int get_blocks_y(){return blocks_y;}
	void set_dir(int dir){show_dir = dir;}
	void set_type(int type){show_type = type;}
	int get_block_size_x_(){return block_size_x[show_type] / zooming;}
	int get_block_size_y_(){return block_size_y[show_type] / zooming;}
	int get_sx(int x){return get_block_size_x() * x;}
	int get_sy(int y){return get_block_size_y() * y;}
	int get_sx_(int x){return get_block_size_x_() * x;}
	int get_sy_(int y){return get_block_size_y_() * y;}
	String getWindowTitle(){return title + " - " + show_dir_str[show_dir] + " - " + show_type_str[show_type];}
	int getIndex(int i){
		switch(show_dir){
			case 0:	//行を優先
				return i;
			case 1:	//列を優先
				int x = i / blocks_y, y = i % blocks_y;
				return y * blocks_x + x;
			case 2:	//ハイブリッド
				return index[i];
		}
		return 0;
	}
	/* 画像処理 */
	void addSpecialFrame(BufferedImage image, int px1, int py1, int px2, int py2){
		Graphics2D graphics2d = (Graphics2D)image.getGraphics();
		graphics2d.setStroke(wideStroke);
		graphics2d.setPaint(Color.BLUE);
		switch(show_dir){
			case 0:	//行を優先
				for(int y = 1; y < blocks_y; y++){
					for(int x = 0; x < blocks_x; x++){
						if((y != py1) && (y != py2 + 1)){
							graphics2d.draw(new Line2D.Double(get_sx(x), get_sy(y), get_sx(x + 1), get_sy(y)));
						}
					}
				}
				break;
			case 1:	//列を優先
				for(int x = 1; x < blocks_x; x++){
					for(int y = 0; y < blocks_y; y++){
						if((x != px1) && (x != px2 + 1)){
							graphics2d.draw(new Line2D.Double(get_sx(x), get_sy(y), get_sx(x), get_sy(y + 1)));
						}
					}
				}
				break;
			case 2:	//艦隊優先
				for(int x = 0; x < blocks_x; x++){
					if((3 != py1) && (3 != py2 + 1)){
						graphics2d.draw(new Line2D.Double(get_sx(x), get_sy(3), get_sx(x + 1), get_sy(3)));
					}
				}
				for(int y = 0; y < blocks_y; y++){
					if((2 != px1) && (2 != px2 + 1)){
						graphics2d.draw(new Line2D.Double(get_sx(2), 0, get_sx(2), get_sy(blocks_y)));
					}
					if((4 != px1) && (4 != px2 + 1)){
						graphics2d.draw(new Line2D.Double(get_sx(4), 0, get_sx(4), get_sy(blocks_y)));
					}
				}
				break;
		}
		graphics2d.setPaint(Color.WHITE);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Font font = new Font("Arial", Font.PLAIN, 40);
		graphics2d.setFont(font);
		for(int i = 0; i < blocks_x * blocks_y; i++){
			int p = getIndex(i);
			int px = p % blocks_x, py = p / blocks_x;
			graphics2d.drawString("" + (i / blocks_x + 1) + "-" + ((i % blocks_x) + 1), get_sx(px + 1) - 70, get_sy(py) + 40);
		}
		graphics2d.setPaint(Color.RED);
		graphics2d.setFont(font);
		for(int i = 0; i < blocks_x * blocks_y; i++){
			int p = getIndex(i);
			int px = p % blocks_x, py = p / blocks_x;
			graphics2d.drawString("" + (i / blocks_x + 1) + "-" + ((i % blocks_x) + 1), get_sx(px + 1) - 72, get_sy(py) + 38);
		}
		graphics2d.dispose();
	}
	/* 画像判定 */
	boolean checkImage(BufferedImage image){
		return checkColor(image, 300, 172, 241, 191, 119);
	}
	int checkImageX(BufferedImage image){
		if(!checkColor(image, 300, 172, 241, 191, 119)) return -1;
		for(int i = 0; i < 4; i++){
			if(checkColor(image, 136 + 30 * i, 122, 33, 150, 151)){
				for(int j = 0; j < 6; j++){
					if(checkColor(image, 294, 145 + 54 * j, 255, 102, 29)){
						return i * 6 + j;
					}
				}
			}
		}
		return -1;
	}
}
