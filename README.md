#### HM-Pay

集成了支付，银行卡四要素绑定，消费历史记录，次卡充值，选择支付方式

#### 功能介绍
- 支付，次卡消耗
- 银行卡四要素绑定
- 消费历史记录
- 次卡充值
- 选择支付方式

#### 安装

在工程根目录的build.gradle文件里添加如下maven地址：

```gradle
allprojects {
    repositories {
        ...
        maven { url 'file:///Users/syl/.m2/repository/' }
        ...
    }
}
```

在项目模块的buile.gradle文件里面加入如下依赖：

```gradle
    compile 'com.heima.iou:hmpaylocal:1.0.0'
```

外部引用：

```gradle
    compile 'com.heima.iou:hmbasebizlocal:1.0.0'
```

#### 使用说明

支持的路由

| 页面 | 路由url | 备注 |
| ------ | ------ | ------ |
| 支付，次卡消耗 | hmiou://m.54jietiao.com/pay/expend_time_card_num|  |
| 银行卡四要素绑定 | hmiou://m.54jietiao.com/login/guide |  |
| 消费历史记录 | hmiou://m.54jietiao.com/pay/history |  |
| 次卡充值 | hmiou://m.54jietiao.com/pay/time_card_recharge |  |
| 选择支付方式 | hmiou://m.54jietiao.com/pay/select_pay_type?time_card_num=* &time_card_money=* &time_card_unit_price=* &package_id=* |  |

路由文件

```json
{
  "pay": [
    {
      "url": "hmiou://m.54jietiao.com/pay/select_pay_type?time_card_num=*&time_card_money=*&time_card_unit_price=*&package_id=*",
      "iclass": "",
      "aclass": "com.hm.iou.pay.business.type.SelectPayTypeActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/pay/time_card_recharge",
      "iclass": "",
      "aclass": "com.hm.iou.pay.business.timecard.view.TimeCardRechargeActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/pay/history",
      "iclass": "",
      "aclass": "com.hm.iou.pay.business.history.view.HistoryActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/pay/expend_time_card_num",
      "iclass": "",
      "aclass": "com.hm.iou.pay.business.expend.view.ExpendActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/pay/user_bind_bank",
      "iclass": "",
      "aclass": "com.hm.iou.pay.business.fourelements.RealNameActivity"
    }
  ]
  }
```

#### 集成说明

集成本模块之前，需要保证一下模块已经初始化：

HM-BaseBiz初始化(基础业务模块)，HM-Network（网络框架），HM-Router（路由模块），Logger（日志记录）

#### Author

syl