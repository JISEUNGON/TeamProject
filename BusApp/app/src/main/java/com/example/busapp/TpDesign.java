package com.example.busapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;

public class TpDesign extends TimePicker {



        // 구분 라인 색상
        private final int m_iColor = 0xFFA561BD;

        public TpDesign( Context context )
        {
            super( context );
            Create( context, null );
        }



        public TpDesign( Context context, AttributeSet attrs )
        {
            super( context, attrs );
            Create( context, attrs );
        }



        public TpDesign( Context context, AttributeSet attrs, int defStyle )
        {
            super( context, attrs, defStyle );
            Create( context, attrs );
        }



        private void Create( Context clsContext, AttributeSet attrs )
        {
            try
            {
                Class<?> clsParent = Class.forName( "com.android.internal.R$id" );
                NumberPicker clsAmPm = (NumberPicker)findViewById( clsParent.getField( "amPm" ).getInt( null ) );
                NumberPicker clsHour = (NumberPicker)findViewById( clsParent.getField( "hour" ).getInt( null ) );
                NumberPicker clsMin = (NumberPicker)findViewById( clsParent.getField( "minute" ).getInt( null ) );
                Class<?> clsNumberPicker = Class.forName( "android.widget.NumberPicker" );
                @SuppressLint("SoonBlockedPrivateApi") Field clsSelectionDivider = clsNumberPicker.getDeclaredField( "mSelectionDivider" );
                clsSelectionDivider.setAccessible( true );
                ColorDrawable clsDrawable = new ColorDrawable( m_iColor );



                // 오전/오후, 시, 분 구분 라인의 색상을 변경한다.
                clsSelectionDivider.set( clsAmPm, clsDrawable );
                clsSelectionDivider.set( clsHour, clsDrawable );
                clsSelectionDivider.set( clsMin, clsDrawable );
            }
            catch( Exception e )
            {
                e.printStackTrace( );
            }
        }
    }