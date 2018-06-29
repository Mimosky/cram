package org.app.client.application.initconges;

import java.util.List;

import org.app.client.application.toolbox.CurrentUser;
import org.app.shared.dispatch.RetrieveUsersAction;
import org.app.shared.dispatch.RetrieveUsersResult;
import org.app.shared.wrapper.UserWrapper;

import com.github.gwtbootstrap.client.ui.CellTable;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
public class InitCongesPresenter extends Presenter<InitCongesPresenter.MyView, InitCongesPresenter.MyProxy>  {
    interface MyView extends View  {
    	 public CellTable<UserWrapper> getCongesTable();
    }


    
    @ProxyStandard
    interface MyProxy extends Proxy<InitCongesPresenter> {
    }

    @Inject
    InitCongesPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy, CurrentUser currentUser) {
        super(eventBus, view, proxy);
        this.currentUser = currentUser;
        
    }
    private CurrentUser currentUser;
	private ListDataProvider<UserWrapper> dataProvider;
	@Inject
	RpcDispatchAsync dispatchAsync;
	
	@Override
	protected void onBind() {
		super.onBind();
		prepareTable();
		addRowToUserTable();
		
	}
	
	
	protected void prepareTable() {
		
		getView().getCongesTable().setPageSize(10);

		TextColumn<UserWrapper> nameColumn = new TextColumn<UserWrapper>() {
			
			@Override
			public String getValue(UserWrapper user) {
				return user.getUserName();
			}
		};
		nameColumn.setFieldUpdater(new FieldUpdater<UserWrapper, String>() {
			
			@Override
			public void update(int index, UserWrapper user, String value) {
				user.setUserName(value);
			}
		});
		
		// Make the name column sortable.
		// typeColumn.setSortable(true);

		TextColumn<UserWrapper> surNameColumn = new TextColumn<UserWrapper>() {
			@Override
			public String getValue(UserWrapper user) {
				return user.getUserSurname();
			}
		};
		

		Column<UserWrapper, String> oldCpColumn = new Column<UserWrapper, String>(new EditTextCell()){
			@Override
			public String getValue(UserWrapper user) {
				return user.getOldCP();
			}
		};
		
		
		Column<UserWrapper, String> previousCpColumn = new Column<UserWrapper, String>(new EditTextCell()){
			@Override
			public String getValue(UserWrapper user) {
				return user.getPreviousCP();
			}
		};
		
		Column<UserWrapper, String> currentCpColumn = new Column<UserWrapper, String>(new EditTextCell()){
			@Override
			public String getValue(UserWrapper user) {
				return user.getCurrentCP();
			}
		};
		
		Column<UserWrapper, String> rttColumn = new Column<UserWrapper, String>(new EditTextCell()){
			@Override
			public String getValue(UserWrapper user) {
				return user.getRTT();
			}
		};
		Column<UserWrapper, String> extras = new Column<UserWrapper, String>(new EditTextCell()){
			@Override
			public String getValue(UserWrapper user) {
				return user.getExtras();
			}
		};
		
		Column<UserWrapper, String> epargne = new Column<UserWrapper, String>(new EditTextCell()){
			@Override
			public String getValue(UserWrapper user) {
				return user.getEpargne();
			}
		};


		// Add     the columns.
		getView().getCongesTable().addColumn(nameColumn, "Nom");
		getView().getCongesTable().addColumn(surNameColumn, "Prénom");
		
		getView().getCongesTable().addColumn(oldCpColumn, "N-2");
		getView().getCongesTable().addColumn(previousCpColumn, "N-1");
		getView().getCongesTable().addColumn(currentCpColumn, "N");
		getView().getCongesTable().addColumn(rttColumn, "RTT");
		getView().getCongesTable().addColumn(extras, "Extras");
		getView().getCongesTable().addColumn(epargne, "Epargne");
		getView().getCongesTable().setWidth("100%",true);
		getView().getCongesTable().setColumnWidth(oldCpColumn, 15.0, Unit.PCT);
		dataProvider = new ListDataProvider<UserWrapper>();

		// Connect the table to the data provider.
		dataProvider.addDataDisplay(getView().getCongesTable());

	}
	
	
	protected void addRowToUserTable() {

		dispatchAsync.execute(new RetrieveUsersAction(), new AsyncCallback<RetrieveUsersResult>() {

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(RetrieveUsersResult response) {
//				Logger logger = Logger.getLogger("MS");
//				logger.log(Level.SEVERE, "Retrieve User On success : " + arg0.getResult().size());
				List<UserWrapper> list = dataProvider.getList();
				list.clear();
				for (UserWrapper i : response.getResult()) {
					if(i.getStatus().equals("enabled")){
						list.add(i);
					}
				}
				dataProvider.flush();
				dataProvider.refresh();
				
			}
		});
	}
}