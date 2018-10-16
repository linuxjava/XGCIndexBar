# XGCPinnedDecoration
通过RecyclerView的Decoration实现分组固定Header，支持任意Item类型实现固定Header。

##  APK下载
[Download](https://github.com/linuxjava/XGCPinnedDecoration/raw/master/apk/app-debug.apk)

## 使用
### 添加依赖
```xml
implementation 'xgc.free.pinned.decoration:PinnedDecoration:0.1'
```
### 代码调用
只需这一句即可实现，registerTypePinnedHeader注册一个PinnedHeaderCreator，指定其ItemView的类型即可
```xml
mRecyclerView.addItemDecoration(new PinnedHeaderDecoration().registerTypePinnedHeader(LData.ITEM_TYPE_SECTION
                , new PinnedHeaderDecoration.PinnedHeaderCreator() {
                    @Override
                    public boolean create(RecyclerView parent, int adapterPosition) {
                        return true;
                    }
                }));
```
![image](https://github.com/linuxjava/XGCPinnedDecoration/raw/master/screenshot/1.gif)





