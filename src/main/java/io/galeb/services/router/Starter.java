package io.galeb.services.router;

import io.galeb.services.cdi.WeldContext;

public class Starter {

    public static void main(String[] args) {

        WeldContext.INSTANCE.getBean(Router.class);

    }

}
