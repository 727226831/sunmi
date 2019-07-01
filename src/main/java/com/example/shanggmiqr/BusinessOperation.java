package com.example.shanggmiqr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.example.shanggmiqr.transaction.AllocateTransfer;
import com.example.shanggmiqr.transaction.LoanBill;
import com.example.shanggmiqr.transaction.OtherEntry;
import com.example.shanggmiqr.transaction.ProductEntry;
import com.example.shanggmiqr.transaction.PurchaseArrival;
import com.example.shanggmiqr.transaction.PurchaseReturn;
import com.example.shanggmiqr.transaction.SaleDelivery;
import com.example.shanggmiqr.util.MyImageView;
import com.example.shanggmiqr.util.Utils;
import com.example.weiytjiang.shangmiqr.R;

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
    Intent intent;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sale_delivery:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                intent=new Intent(BusinessOperation.this,SaleDelivery.class);
                intent.putExtra("from_business_operation", "Y");
                intent.putExtra("type",0);
                startActivity(intent);
                break;
            case R.id.other_entry:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                intent=new Intent(BusinessOperation.this,OtherEntry.class);
                intent.putExtra("from_business_operation", "Y");
                intent.putExtra("type",1);
                startActivity(intent);
                break;
            case R.id.other_outgoing:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                intent=new Intent(BusinessOperation.this,OtherEntry.class);
                intent.putExtra("from_business_operation", "Y");
                intent.putExtra("type",2);
                startActivity(intent);
                break;
            case R.id.product_entry:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                intent=new Intent(BusinessOperation.this,ProductEntry.class);
                intent.putExtra("from_business_operation", "Y");
                intent.putExtra("type",3);
                startActivity(intent);
                break;
            case R.id.loan:
                //借出
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                intent=new Intent(BusinessOperation.this,LoanBill.class);
                intent.putExtra("from_business_operation", "Y");
                intent.putExtra("type",4);
                startActivity(intent);
                break;
            case R.id.allocate:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                intent=new Intent(BusinessOperation.this,AllocateTransfer.class);
                intent.putExtra("from_business_operation", "Y");
                startActivity(intent);
                break;

            case R.id.purchase_arrival:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                intent=new Intent(BusinessOperation.this,PurchaseArrival.class);
                intent.putExtra("from_business_operation", "Y");
                startActivity(intent);

                break;
            case R.id.purchase_return:
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                intent=new Intent(BusinessOperation.this,PurchaseReturn.class);
                intent.putExtra("from_business_operation", "Y");
                startActivity(intent);

                break;

        }
    }

}
