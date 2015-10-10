/* 記録は大切なの.jar Ver.1.1 */

/* TODO:
 * 以上を実装したらリリースする
 */

public class nano{
	public static main_window main_frame;
	public static unit_window unit_frame;
	public static sort_window sort_frame;
	public static option_window option_frame;
	public static void main(String args[]){
		// 改装一覧用
		unit_frame = new unit_window();
		// ソート一覧用
		sort_frame = new sort_window();
		// メイン画面
		main_frame = new main_window();
		// オプション画面
		option_frame = new option_window();
	}
}
