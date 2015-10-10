/* 画像取得用 */

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class capture{
	/* メンバ変数 */
	// 定数
	public static final int flash_x = 800;
	public static final int flash_y = 480;
	static final int judge_x = flash_x + 2;
	static final int judge_y = flash_y + 2;
	static final int white = 0xffffff;
	// 変数
	static ArrayList<Integer> gd_index, gc_index;
	static int display_index = -1, flash_px, flash_py;
	/* 艦これの座標を取得する */
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
			int n = 0;
			for(BufferedImage image : images){
				int width  = image.getWidth();
				int height = image.getHeight();
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
						if((image.getRGB(x    , y    ) & white) != white) continue;
						if((image.getRGB(x + 1, y    ) & white) != white) continue;
						if((image.getRGB(x    , y + 1) & white) != white) continue;
						if((image.getRGB(x + 1, y + 1) & white) == white) continue;
						// 「右上」
						if((image.getRGB(x + flash_x    , y    ) & white) != white) continue;
						if((image.getRGB(x + flash_x + 1, y    ) & white) != white) continue;
						if((image.getRGB(x + flash_x    , y + 1) & white) == white) continue;
						if((image.getRGB(x + flash_x + 1, y + 1) & white) != white) continue;
						// 「左下」
						if((image.getRGB(x    , y + flash_y    ) & white) != white) continue;
						if((image.getRGB(x + 1, y + flash_y    ) & white) == white) continue;
						if((image.getRGB(x    , y + flash_y + 1) & white) != white) continue;
						if((image.getRGB(x + 1, y + flash_y + 1) & white) != white) continue;
						// 「右下」
						if((image.getRGB(x + flash_x    , y + flash_y    ) & white) == white) continue;
						if((image.getRGB(x + flash_x + 1, y + flash_y    ) & white) != white) continue;
						if((image.getRGB(x + flash_x    , y + flash_y + 1) & white) != white) continue;
						if((image.getRGB(x + flash_x + 1, y + flash_y + 1) & white) != white) continue;
						// 検出できたので、そのディスプレイの番号および座標を取得する
						display_index = n;
						flash_px = x + 1;
						flash_py = y + 1;
						break;
					}
					if(display_index >= 0) break;
				}
				n++;
			}
			if(display_index >= 0){
				main_window.putLog("ディスプレイ番号-左上座標：" + display_index + "-" + flash_px + "," + flash_py);
			}else{
				main_window.putLog("艦これの画面を取得できませんでした。");
			}
		}
		catch(Exception error){
			error.printStackTrace();
		}
	}
	/* 艦これの画面を取得する */
	static public BufferedImage getImage(){
		try{
			if(display_index < 0) return null;
			GraphicsDevice[] all_gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
			GraphicsConfiguration[] all_gc = all_gd[gd_index.get(display_index)].getConfigurations();
			Robot robot = new Robot(all_gd[gd_index.get(display_index)]);
			return robot.createScreenCapture(all_gc[gc_index.get(display_index)].getBounds()).getSubimage(flash_px, flash_py, flash_x, flash_y);
		}
		catch(Exception error){
			error.printStackTrace();
			return null;
		}
	}
}
