package com.openvraas.services.router;

import com.openvraas.services.cdi.WeldContext;

public class Starter {

    public static void main(String[] args) {

        WeldContext.INSTANCE.getBean(Router.class);

    }

}
