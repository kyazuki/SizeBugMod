# SizeBug Mod
10秒毎にサイズが変わる世界

## 対応バージョン
Minecraft: 1.16.x<br>
Minecraft Forge: 32.0.0以上<br>
http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.16.1.html

## 概説
10秒ごとにプレイヤーのサイズがランダムに変更されます。

## configファイル
Modを導入した状態で一回でも起動すると、configフォルダ内にsizebugmod-common.tomlが生成されます。<br>
値の変更は次回の起動時に適用されます。
- timer
  - サイズ変更が起こるインターバルの秒数です。<br>
    0～1000の間で指定でき、デフォルトは10です。
- max_width
  - ウエストの最大倍率です。<br>
    0.0～1000.0の間で指定でき、デフォルトは10.0です。
- max_height
  - 身長の最大倍率です。<br>
    0.0～1000.0の間で指定でき、デフォルトは10.0です。
- change_eyeheight
  - プレイヤーの視点の高さを変更するかどうか。<br>
    trueかfalseで指定でき、デフォルトはtrueです。
- change_hitbox
  - プレイヤーの当たり判定を変更するかどうか。<br>
    trueかfalseで指定でき、デフォルトはtrueです。

## 注意点
- 三人称視点でプレイヤーを見るとき、視点によってはプレイヤーが歪んで見える場合があります。