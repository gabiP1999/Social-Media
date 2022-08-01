package bgu.spl.net.BGS.Messages;

public class BGSDate {
    private int day;
    private int month;
    private int year;
    public BGSDate(int day,int month,int year){
        this.day=day;this.month=month;this.year=year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
    public int getAge(){
        return 2022-year;
    }

    @Override
    public String toString() {
        return "BGSDate{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
