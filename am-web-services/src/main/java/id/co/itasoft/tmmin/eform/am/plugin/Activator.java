package id.co.itasoft.tmmin.eform.am.plugin;

import java.util.ArrayList;
import java.util.Collection;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    protected Collection<ServiceRegistration> registrationList;

    public void start(BundleContext context) {
        registrationList = new ArrayList<ServiceRegistration>();

        //Register plugin here
        registrationList.add(context.registerService(LoadDataEmployee.class.getName(), new LoadDataEmployee(), null));
        registrationList.add(context.registerService(LoadDataCaScore.class.getName(), new LoadDataCaScore(), null));
        registrationList.add(context.registerService(GetApproverMapping.class.getName(), new GetApproverMapping(), null));
        registrationList.add(context.registerService(KickStartProcessCARequest.class.getName(), new KickStartProcessCARequest(), null));
        registrationList.add(context.registerService(DelegateAllJobs.class.getName(), new DelegateAllJobs(), null));
    }

    public void stop(BundleContext context) {
        for (ServiceRegistration registration : registrationList) {
            registration.unregister();
        }
    }
}