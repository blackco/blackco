package blackco.photos;

import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;


public class Services {

	
		public enum Service{ FLICKR_AUTH};
		
		private static HashMap<Service, Object> services = new HashMap<Service, Object>();
		
		private static synchronized void initServices(){
			
			if ( services == null){
				services = new HashMap<Service,Object>();
			}
		}
		
		public static HashMap<Service, Object> getServices(){

			if ( services == null){
		
				initServices();
				
			}
			
			return services;
			
		}
		
		public static void addService(Service service, Object obj){
			services.put(service, obj);
		}

		public static Object getService(Service service){
			
			return getServices().get(service);
		}
		
		
		
		
	
}
