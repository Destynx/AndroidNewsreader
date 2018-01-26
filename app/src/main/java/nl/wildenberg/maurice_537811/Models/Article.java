package nl.wildenberg.maurice_537811.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mdwil on 8-10-2017.
 */

public class Article implements Parcelable{
    public int Id;
    public int Feed;
    public String Title;
    public String Summary;
    public String PublishDate;
    public String Image;
    public String Url;
    public ArrayList<String> Related;
    public ArrayList<Category> Categories;
    public boolean IsLiked;

    protected Article(Parcel in) {
        Id = in.readInt();
        Feed = in.readInt();
        Title = in.readString();
        Summary = in.readString();
        PublishDate = in.readString();
        Image = in.readString();
        Url = in.readString();
        Related = in.createStringArrayList();
        IsLiked = in.readByte() != 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeInt(Feed);
        parcel.writeString(Title);
        parcel.writeString(Summary);
        parcel.writeString(PublishDate);
        parcel.writeString(Image);
        parcel.writeString(Url);
        parcel.writeStringList(Related);
        parcel.writeByte((byte) (IsLiked ? 1 : 0));
    }
}
