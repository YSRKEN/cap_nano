/* 記録は大切なの.jar Ver.1.2 */

/* TODO:
 * 実紹
 * 装介
 * ○○早めにdisposeしておく
 * ○　改装及びソート一覧のデフォルトでの表示位置を変更
 * ○　画像全削除機能(画像保存時に確認する形)
 * ○○自動画像取得機能(取得速度は1fps程度にしておく)
 * ○○名前隠し機能(自身および演習相手の名前)
 * ○○連射機能(一応速度調整できるようにしておく)
 * ○○クロップ機能―編成画面で編成部分だけ切り取って保存
 * ○○クロップ機能―母港で資源および時計部分だけ切り取って保存
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
