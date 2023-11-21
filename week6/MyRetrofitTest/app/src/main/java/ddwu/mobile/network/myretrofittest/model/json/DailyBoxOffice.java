package ddwu.mobile.network.myretrofittest.model.json;

public class DailyBoxOffice {
    private String rnum;
    private String movieCd;
    private String movieNm;
    private String openDt;

    public String getRnum() {
        return rnum;
    }

    public void setRnum(String rnum) {
        this.rnum = rnum;
    }

    public String getMovieCd() {
        return movieCd;
    }

    public void setMovieCd(String movieCd) {
        this.movieCd = movieCd;
    }

    public String getMovieNm() {
        return movieNm;
    }

    public void setMovieNm(String movieNm) {
        this.movieNm = movieNm;
    }

    public String getOpenDt() {
        return openDt;
    }

    public void setOpenDt(String openDt) {
        this.openDt = openDt;
    }

    @Override
    public String toString() {
        return "DailyBoxOffice{" +
                "rnum='" + rnum + '\'' +
                ", movieNm='" + movieNm + '\'' +
                '}';
    }
}
