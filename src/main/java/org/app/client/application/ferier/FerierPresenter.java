package org.app.client.application.ferier;

import org.app.shared.dispatch.AddFerierAction;
import org.app.shared.dispatch.AddFerierResult;
import org.app.shared.dispatch.RetrieveFerierAction;
import org.app.shared.dispatch.RetrieveFerierResult;
import org.app.shared.wrapper.FerierWrapper;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
public class FerierPresenter extends Presenter<FerierPresenter.MyView, FerierPresenter.MyProxy>  {
    interface MyView extends View  {
        public DateBox getFerier() ;
        public ListBox getListBox() ;
        public CellTable<FerierWrapper> getFerierTable() ;
        public Button getAddFerier() ;
    }

    
    @ProxyStandard
    interface MyProxy extends Proxy<FerierPresenter> {
    }

    @Inject
    FerierPresenter(
            EventBus eventBus,
            MyView view, 
            MyProxy proxy) {
        super(eventBus, view, proxy);
        
    }
    
	private ListDataProvider<FerierWrapper> dataProvider;
	@Inject RpcDispatchAsync dispatchAsync;
    
	@Override
	protected void onBind() {
		super.onBind();
		prepareTable();
		addRowToFerierTable();
		getView().getAddFerier().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				dispatchAsync.execute(new AddFerierAction(getView().getFerier().getValue(), getView().getListBox().getValue()), new AsyncCallback<AddFerierResult>() {

					@Override
					public void onFailure(Throwable error) {
						Window.alert(error.toString());
						
					}

					@Override
					public void onSuccess(AddFerierResult result) {
						addRowToFerierTable();
					}
				});
			}
		});
	}
    
    protected void prepareTable() {
		getView().getFerierTable().setPageSize(10);
		// Create name column.
		TextColumn<FerierWrapper> dateColumn = new TextColumn<FerierWrapper>() {
			@Override
			public String getValue(FerierWrapper jour) {
				return jour.getFomatedDate();
			}
		};

		// Make the name column sortable.
		dateColumn.setSortable(true);

		// Create Start Date column.
		TextColumn<FerierWrapper> typeColumn = new TextColumn<FerierWrapper>() {
			@Override
			public String getValue(FerierWrapper jour) {
				return jour.getType();
			}
		};

		
//
//		ButtonCell buttonCell = new ButtonCell(IconType.REMOVE, ButtonType.DANGER);
//		Column<Ferier, String> buttonCol = new Column<Ferier, String>(buttonCell) {
//			@Override
//			public String getValue(Ferier object) {
//				return "supprimer";
//			}
//		};
		// Add the columns.
		getView().getFerierTable().addColumn(dateColumn, "Date");
		getView().getFerierTable().addColumn(typeColumn, "Type");
	//	getView().getFerierTable().addColumn(buttonCol);

		dataProvider = new ListDataProvider<FerierWrapper>();
		dataProvider.addDataDisplay(getView().getFerierTable());

//		buttonCol.setFieldUpdater(new FieldUpdater<Ferier, String>() {
//			@Override
//			public void update(int index, Ferier object, String value) {
//				Ferier jour = dataProvider.getList().get(dataProvider.getList().indexOf(object));
//				//TODO
//			}
//		});
	}
    
    public void addRowToFerierTable(){
    	dispatchAsync.execute(new RetrieveFerierAction(), new AsyncCallback<RetrieveFerierResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(RetrieveFerierResult result) {
//				Logger logger = Logger.getLogger("MS");
//				logger.log(Level.SEVERE, "Retrieve Ferier On success" + result.getResult().size());
				dataProvider.setList(result.getResult());
//				List<FerierWrapper> list = dataProvider.getList();
//				list.clear();
//				list = result.getResult();

//				dataProvider.flush();
//				dataProvider.refresh();
			}
		});
    }

    
}