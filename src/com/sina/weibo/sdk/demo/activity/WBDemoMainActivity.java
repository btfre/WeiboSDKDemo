/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sina.weibo.sdk.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sina.weibo.sdk.demo.R;
import com.sina.weibo.sdk.demo.openapi.WBOpenAPIActivity;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 该类是整个 DEMO 程序的入口。
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBDemoMainActivity extends Activity {

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.sIsLogEnable = true;
        
        // 微博授权功能
        this.findViewById(R.id.feature_oauth).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBAuthActivity.class));
            }
        });

        
        // 社会化组件
        this.findViewById(R.id.social_component).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBSocialActivity.class));
            }
        });
        
        
        // 开放接口（Open API）功能
        this.findViewById(R.id.feature_open_api).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBOpenAPIActivity.class));
            }
        });
    }
}
