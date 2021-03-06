package org.app.shared.wrapper;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WorkLoadWrapper implements IsSerializable {
	// The client Name
		private String clientName ;
		// The value should be: M, A or J for (Matin, Après-midi, Journée)
		private String dayLoad;
		// The load value should be 0, 0.5 or 1
		private float loadr;
		
		public WorkLoadWrapper(String clientName, String dayLoad, float load) {
			super();
			this.clientName = clientName;
			this.dayLoad = dayLoad;
			this.loadr = load;
		}
		
		public WorkLoadWrapper(){
			super();
		}

		public String getClientName() {
			return clientName;
		}

		public void setClientName(String clientName) {
			this.clientName = clientName;
		}

		public String getDayLoad() {
			return dayLoad;
		}

		public void setDayLoad(String dayLoad) {
			this.dayLoad = dayLoad;
		}

		public float getLoad() {
			return loadr;
		}

		public void setLoad(float load) {
			this.loadr = load;
		}

		public void removeDayLoad(){
				this.dayLoad ="";
		}
		public void addDayLoad(String dayLoad){
			if(this.dayLoad.equals(""))
				this.dayLoad = dayLoad;
			else
				this.dayLoad +="-"+dayLoad;
		}
		
		public void removeClientName(){
				this.clientName ="";
		}

		public void addClientName(String clientName){
			if(this.clientName.equals(""))
				this.clientName = clientName;
			else
				this.clientName +="-"+clientName;
		}
		public void addLoad(float load){
			this.loadr += load;
		}
		
		public void removeLoad(){
			this.loadr = 0;
		}
		
		@Override
		public String toString() {
			return clientName+"|"+dayLoad+"|"+loadr+"#";
		}
}
