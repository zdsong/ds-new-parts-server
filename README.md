#### 新部品管理システム｜外部倉庫管理
* Server: Spring Boot v3.5.8、Spring Security、Redis & Jwt。
*         指定JDK（Amazon Corretto 21）バージョンを使用する，ダウンロードアドレス（https://docs.aws.amazon.com/zh_cn/corretto/latest/corretto-21-ug/downloads-list.html）
*

#### モジュールの説明
  ds-new-parts-server
     --external-warehouse                   // external-warehouse: フォアグラウンドインタフェース定義モジュール
     --external-warehouse-biz               // external-warehouse: ビジネスモジュール
     --external-warehouse-common            // 共通処理モジュール
     --external-warehouse-quartz            // スケジュールタスクモジュール

#### .ymlファイルの説明
  prod.yml: 日本本番環境用の設定ファイル
  stg1.yml: 日本Dev環境用の設定ファイル
  stg2.yml: 日本ステージング環境用の設定ファイル
  test.yml: 大連テスト環境用の設定ファイル