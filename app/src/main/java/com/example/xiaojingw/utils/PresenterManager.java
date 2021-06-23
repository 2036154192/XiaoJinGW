package com.example.xiaojingw.utils;

import com.example.xiaojingw.presenter.ICategoryPagerPresenter;
import com.example.xiaojingw.presenter.IHomePresenter;
import com.example.xiaojingw.presenter.IOnSellPagePresenter;
import com.example.xiaojingw.presenter.ISearchPresenter;
import com.example.xiaojingw.presenter.ISelectdPagePresenter;
import com.example.xiaojingw.presenter.ITicketPresenter;
import com.example.xiaojingw.presenter.impl.CategoryPagePresenterlmpl;
import com.example.xiaojingw.presenter.impl.HomePresenterImpl;
import com.example.xiaojingw.presenter.impl.OnSellPagePresenterlmpl;
import com.example.xiaojingw.presenter.impl.SearchPresenter;
import com.example.xiaojingw.presenter.impl.SelectedPagePresenterImpl;
import com.example.xiaojingw.presenter.impl.TicketPresenterlmpl;

public class PresenterManager {

    private static final PresenterManager ourInstance = new PresenterManager();
    private final ICategoryPagerPresenter categoryPagePresenterlmpl;
    private final IHomePresenter homePresenter;
    private final ITicketPresenter ticketPresenterlmpl;
    private final ISelectdPagePresenter selectedPagePresenter;
    private final IOnSellPagePresenter onSellPagePresenter;
    private final ISearchPresenter searchPresenter;

    public static PresenterManager getInstance(){
        return ourInstance;
    }

    private PresenterManager(){
        categoryPagePresenterlmpl = new CategoryPagePresenterlmpl();
        homePresenter = new HomePresenterImpl();
        ticketPresenterlmpl = new TicketPresenterlmpl();
        selectedPagePresenter = new SelectedPagePresenterImpl();
        onSellPagePresenter = new OnSellPagePresenterlmpl();
        searchPresenter = new SearchPresenter();
    }

    public ICategoryPagerPresenter getCategoryPagePresenterlmpl() {
        return categoryPagePresenterlmpl;
    }

    public ISearchPresenter getSearchPresenter() {
        return searchPresenter;
    }

    public IOnSellPagePresenter getOnSellPagePresenter() {
        return onSellPagePresenter;
    }

    public IHomePresenter getHomePresenter() {
        return homePresenter;
    }

    public ITicketPresenter getTicketPresenterlmpl() {
        return ticketPresenterlmpl;
    }

    public ISelectdPagePresenter getSelectedPagePresenter() {
        return selectedPagePresenter;
    }
}
