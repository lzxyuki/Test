package com.example.dell.testview;

import android.app.PictureInPictureParams;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.leadfView)
    LeafAnimView leadfView;
    @BindView(R.id.drawer_scroll_ly)
    VerticalDrawerLayout drawerScrollLy;
    @BindView(R.id.drawer_scroll_rel)
    RelativeLayout drawerScrollRel;
    @BindView(R.id.drawer_scroll)
    DrawerScrollLayout drawerScroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        drawerScrollLy.setCanScroll(true);
        drawerScroll.setVisibility(View.VISIBLE);
        drawerScrollLy.setOpenChangeListener(new VerticalDrawerLayout.OpenChangeListener() {
            @Override
            public void isOpen(boolean isOpen) {
                if (isOpen) {
                    drawerScrollRel.setVisibility(View.VISIBLE);
                } else {
                    drawerScrollRel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onShowHeightChanging(int showHeight) {
                drawerScrollRel.setVisibility(View.VISIBLE);
                drawerScroll.setShowHeight(showHeight);
            }
        });
    }

//    @OnClick({R.id.start, R.id.stop})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.start:
//                leadfView.start();
//                break;
//            case R.id.stop:
//                leadfView.stop();
//                break;
//        }
//    }
}
