package ddwu.mobile.network.myretrofittest.model.json;

import java.util.List;

public class BoxOfficeResult {
    private List<DailyBoxOffice> dailyBoxOfficeList;

    public List<DailyBoxOffice> getDailyBoxOfficeList() {
        return dailyBoxOfficeList;
    }

    public void setDailyBoxOfficeList(List<DailyBoxOffice> dailyBoxOfficeList) {
        this.dailyBoxOfficeList = dailyBoxOfficeList;
    }
}
