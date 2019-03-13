package com.example.shanggmiqr.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.TopMenu;
import com.example.shanggmiqr.util.MyImageView;
import com.example.shanggmiqr.util.Utils;

/**
 * Created by weiyt.jiang on 2018/8/8.
 */

public class BusinessOperation extends AppCompatActivity implements MyImageView.OnClickListener{
    //销售发货
    private MyImageView saleDeliveryButtonView;
    //其他入库
    private MyImageView otherEntryButtonView;
    //其他出库
    private MyImageView otherOutgoingButtonView;
    //调拨出库
    private MyImageView allocateButtonView;
    //借出还回
    private MyImageView loanReturnButtonView;
    //采购退货R39
    private MyImageView purchaseReturnButtonView;
    //采购到货R40
    private MyImageView purchaseArrivalButtonView;
    //材料出库
  //  private MyImageView materialOutButtonView;
    //产成品入库
    private MyImageView productEntryButtonView;
    //借出
    private MyImageView loanButtonView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_operation);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        saleDeliveryButtonView = (MyImageView)findViewById(R.id.sale_delivery);
        otherEntryButtonView = (MyImageView)findViewById(R.id.other_entry);
        otherOutgoingButtonView = (MyImageView)findViewById(R.id.other_outgoing);
        allocateButtonView = (MyImageView)findViewById(R.id.allocate);
        purchaseArrivalButtonView = (MyImageView)findViewById(R.id.purchase_arrival) ;
       // loanReturnButtonView = (MyImageView)findViewById(R.id.loan_return);
        purchaseReturnButtonView = (MyImageView)findViewById(R.id.purchase_return);
      //  materialOutButtonView = (MyImageView)findViewById(R.id.material_out);
        productEntryButtonView = (MyImageView)findViewById(R.id.product_entry);
        loanButtonView = (MyImageView)findViewById(R.id.loan);
        saleDeliveryButtonView.setOnClickListener(BusinessOperation.this);
        purchaseArrivalButtonView.setOnClickListener(BusinessOperation.this);
        otherEntryButtonView.setOnClickListener(BusinessOperation.this);
        otherOutgoingButtonView.setOnClickListener(BusinessOperation.this);
        allocateButtonView.setOnClickListener(BusinessOperation.this);
        loanButtonView.setOnClickListener(BusinessOperation.this);
      //  loanReturnButtonView.setOnClickListener(BusinessOperation.this);
        purchaseReturnButtonView.setOnClickListener(BusinessOperation.this);
       // materialOutButtonView.setOnClickListener(BusinessOperation.this);
        productEntryButtonView.setOnClickListener(BusinessOperation.this);
      // allocateButtonView.setVisibility(View.INVISIBLE);
      //  loanReturnButtonView.setVisibility(View.INVISIBLE);
      //  purchaseReturnButtonView.setVisibility(View.INVISIBLE);
      //  materialOutButtonView.setVisibility(View.INVISIBLE);
   //     loanButtonView.setVisibility(View.INVISIBLE);
 //       productEntryButtonView.setVisibility(View.INVISIBLE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(BusinessOperation.this, TopMenu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //点击完返回键，执行的动作
            Intent intent = new Intent(BusinessOperation.this, TopMenu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sale_delivery:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                Intent intent1=new Intent(BusinessOperation.this,SaleDelivery.class);
                intent1.putExtra("from_business_operation", "Y");
                startActivity(intent1);
                break;
            case R.id.other_entry:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                Intent intent2=new Intent(BusinessOperation.this,OtherEntry.class);
                intent2.putExtra("from_business_operation", "Y");
                startActivity(intent2);
                break;
            case R.id.other_outgoing:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                Intent intent3=new Intent(BusinessOperation.this,OtherOutgoing.class);
                intent3.putExtra("from_business_operation", "Y");
                startActivity(intent3);
                break;
            case R.id.product_entry:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                Intent intent4=new Intent(BusinessOperation.this,ProductEntry.class);
                intent4.putExtra("from_business_operation", "Y");
                startActivity(intent4);
                break;
            case R.id.allocate:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                Intent intent5=new Intent(BusinessOperation.this,AllocateTransfer.class);
                intent5.putExtra("from_business_operation", "Y");
                startActivity(intent5);
                break;
            case R.id.loan:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                Intent intent6=new Intent(BusinessOperation.this,LoanBill.class);
                intent6.putExtra("from_business_operation", "Y");
                startActivity(intent6);
                break;
            case R.id.purchase_arrival:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                Intent intent8=new Intent(BusinessOperation.this,PurchaseArrival.class);
                intent8.putExtra("from_business_operation", "Y");
                startActivity(intent8);
                break;
            case R.id.purchase_return:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                Intent intent7=new Intent(BusinessOperation.this,PurchaseReturn.class);
                intent7.putExtra("from_business_operation", "Y");
                startActivity(intent7);
                break;

        }
    }

}
