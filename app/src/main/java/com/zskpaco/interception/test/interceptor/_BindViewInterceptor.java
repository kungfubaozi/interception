package com.zskpaco.interception.test.interceptor;

import android.app.Activity;
import android.view.View;

import com.zskpaco.interception.AnnotationInterceptor;
import com.zskpaco.interception.Element;
import com.zskpaco.interception.Interceptor;
import com.zskpaco.interception.SurroundElement;
import com.zskpaco.interception.VariableElement;

import java.util.Map;

@Interceptor
public class _BindViewInterceptor implements AnnotationInterceptor<BindView> {
    
    @Override
    public Object intercept(Element element, Map<String, Object> annotationValues) {
        Object host = element.getHost();
        View view = null;
        if (host instanceof Activity) {
            view = ((Activity) host).getWindow().getDecorView().findViewById(android.R.id.content);
        }
        if (element instanceof SurroundElement) {
            SurroundElement surroundElement = (SurroundElement) element;
            for (VariableElement variableElement : surroundElement.getVariables()) {
                Map<String, Object> values = (Map<String, Object>) annotationValues.get(
                        variableElement.getSimpleName());
                assert view != null;
                init(view, variableElement, values);
            }
        } else if (element instanceof VariableElement) {
            VariableElement variableElement = (VariableElement) element;
            assert view != null;
            init(view, variableElement, annotationValues);
        }
        return null;
    }

    /**
     * 初始化
     *
     * @param view
     * @param element
     * @param values
     */
    private void init(View view, VariableElement element, Map<String, Object> values) {
        int id = (int) values.get("value");
        boolean inflate = (boolean) values.get("inflate");
        element.setValue(view.findViewById(id));
    }
}
