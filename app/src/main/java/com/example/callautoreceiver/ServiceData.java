package com.example.callautoreceiver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.CipherInputStream;

public class ServiceData {

    private static List<String> country2phone = new ArrayList<>();

    static {
        country2phone.add("+93");
        country2phone.add("+355");
        country2phone.add("+213");
        country2phone.add("+376");
        country2phone.add("+244");
        country2phone.add("+1-268");
        country2phone.add("+54");
        country2phone.add("+374");
        country2phone.add("+61");
        country2phone.add("+43");
        country2phone.add("+994");
        country2phone.add("+1-242");
        country2phone.add("+973");
        country2phone.add("+880");
        country2phone.add("+1-246");
        country2phone.add("+375");
        country2phone.add("+32");
        country2phone.add("+501");
        country2phone.add("+229");
        country2phone.add("+975");
        country2phone.add("+591");
        country2phone.add("+387");
        country2phone.add("+267");
        country2phone.add("+55");
        country2phone.add("+673");
        country2phone.add("+359");
        country2phone.add("+226");
        country2phone.add("+257");
        country2phone.add("+855");
        country2phone.add("+237");
        country2phone.add("+1");
        country2phone.add("+238");
        country2phone.add("+236");
        country2phone.add("+235");
        country2phone.add("+56");
        country2phone.add("+86");
        country2phone.add("+57");
        country2phone.add("+269");
        country2phone.add("+243");
        country2phone.add("+242");
        country2phone.add("+506");
        country2phone.add("+225");
        country2phone.add("+385");
        country2phone.add("+53");
        country2phone.add("+357");
        country2phone.add("+420");
        country2phone.add("+45");
        country2phone.add("+253");
        country2phone.add("+1-767");
        country2phone.add("+1-809");
        country2phone.add("+1-829");
        country2phone.add("+593");
        country2phone.add("+20");
        country2phone.add("+503");
        country2phone.add("+240");
        country2phone.add("+291");
        country2phone.add("+372");
        country2phone.add("+251");
        country2phone.add("+679");
        country2phone.add("+358");
        country2phone.add("+33");
        country2phone.add("+241");
        country2phone.add("+220");
        country2phone.add("+995");
        country2phone.add("+49");
        country2phone.add("+233");
        country2phone.add("+30");
        country2phone.add("+1-473");
        country2phone.add("+502");
        country2phone.add("+224");
        country2phone.add("+245");
        country2phone.add("+592");
        country2phone.add("+509");
        country2phone.add("+504");
        country2phone.add("+36");
        country2phone.add("+354");
        country2phone.add("+91");
        country2phone.add("+62");
        country2phone.add("+98");
        country2phone.add("+964");
        country2phone.add("+353");
        country2phone.add("+972");
        country2phone.add("+39");
        country2phone.add("+1-876");
        country2phone.add("+81");
        country2phone.add("+962");
        country2phone.add("+7");
        country2phone.add("+254");
        country2phone.add("+686");
        country2phone.add("+850");
        country2phone.add("+82");
        country2phone.add("+965");
        country2phone.add("+996");
        country2phone.add("+856");
        country2phone.add("+371");
        country2phone.add("+961");
        country2phone.add("+266");
        country2phone.add("+231");
        country2phone.add("+218");
        country2phone.add("+423");
        country2phone.add("+370");
        country2phone.add("+352");
        country2phone.add("+389");
        country2phone.add("+261");
        country2phone.add("+265");
        country2phone.add("+60");
        country2phone.add("+960");
        country2phone.add("+223");
        country2phone.add("+356");
        country2phone.add("+692");
        country2phone.add("+222");
        country2phone.add("+230");
        country2phone.add("+52");
        country2phone.add("+691");
        country2phone.add("+373");
        country2phone.add("+377");
        country2phone.add("+976");
        country2phone.add("+382");
        country2phone.add("+212");
        country2phone.add("+258");
        country2phone.add("+95");
        country2phone.add("+264");
        country2phone.add("+674");
        country2phone.add("+977");
        country2phone.add("+31");
        country2phone.add("+64");
        country2phone.add("+505");
        country2phone.add("+227");
        country2phone.add("+234");
        country2phone.add("+47");
        country2phone.add("+968");
        country2phone.add("+92");
        country2phone.add("+680");
        country2phone.add("+507");
        country2phone.add("+675");
        country2phone.add("+595");
        country2phone.add("+51");
        country2phone.add("+63");
        country2phone.add("+48");
        country2phone.add("+351");
        country2phone.add("+974");
        country2phone.add("+40");
        country2phone.add("+7");
        country2phone.add("+250");
        country2phone.add("+1-869");
        country2phone.add("+1-758");
        country2phone.add("+1-784");
        country2phone.add("+685");
        country2phone.add("+378");
        country2phone.add("+239");
        country2phone.add("+966");
        country2phone.add("+221");
        country2phone.add("+381");
        country2phone.add("+248");
        country2phone.add("+232");
        country2phone.add("+65");
        country2phone.add("+421");
        country2phone.add("+386");
        country2phone.add("+677");
        country2phone.add("+252");
        country2phone.add("+27");
        country2phone.add("+34");
        country2phone.add("+94");
        country2phone.add("+249");
        country2phone.add("+597");
        country2phone.add("+268");
        country2phone.add("+46");
        country2phone.add("+41");
        country2phone.add("+963");
        country2phone.add("+992");
        country2phone.add("+255");
        country2phone.add("+66");
        country2phone.add("+670");
        country2phone.add("+228");
        country2phone.add("+676");
        country2phone.add("+1-868");
        country2phone.add("+216");
        country2phone.add("+90");
        country2phone.add("+993");
        country2phone.add("+688");
        country2phone.add("+256");
        country2phone.add("+380");
        country2phone.add("+971");
        country2phone.add("+44");
        country2phone.add("+1");
        country2phone.add("+598");
        country2phone.add("+998");
        country2phone.add("+678");
        country2phone.add("+379");
        country2phone.add("+58");
        country2phone.add("+84");
        country2phone.add("+967");
        country2phone.add("+260");
        country2phone.add("+263");
        country2phone.add("+995");
        country2phone.add("+886");
        country2phone.add("+374-97");
        country2phone.add("+90-392");
        country2phone.add("+373-533");
        country2phone.add("+252");
        country2phone.add("+995");
        country2phone.add("+61");
        country2phone.add("+61");
        country2phone.add("+672");
        country2phone.add("+687");
        country2phone.add("+689");
        country2phone.add("+262");
        country2phone.add("+508");
        country2phone.add("+681");
        country2phone.add("+682");
        country2phone.add("+683");
        country2phone.add("+690");
        country2phone.add("+44");
        country2phone.add("+1-264");
        country2phone.add("+1-441");
        country2phone.add("+246");
        country2phone.add("+357");
        country2phone.add("+1-284");
        country2phone.add("+1-345");
        country2phone.add("+500");
        country2phone.add("+350");
        country2phone.add("+1-664");
        country2phone.add("+290");
        country2phone.add("+1-649");
        country2phone.add("+1-670");
        country2phone.add("+1-787and1-939");
        country2phone.add("+1-684");
        country2phone.add("+1-671");
        country2phone.add("+1-340");
        country2phone.add("+852");
        country2phone.add("+853");
        country2phone.add("+298");
        country2phone.add("+299");
        country2phone.add("+594");
        country2phone.add("+590");
        country2phone.add("+596");
        country2phone.add("+262");
        country2phone.add("+358-18");
        country2phone.add("+297");
        country2phone.add("+599");
        country2phone.add("+47");
        country2phone.add("+247");
        country2phone.add("+290");
        country2phone.add("+381");
        country2phone.add("+970");
        country2phone.add("+212");
    }

    public static List<String[]> getContact(Context context) {
        List<String[]> list = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        list.add(new String[]{name, phoneNumber});
                    }
                    phones.close();
                }

            }
        }
        return list;
    }

    public static boolean pureNumber(String numberPlus, String number) {
        try {
            if (numberPlus.endsWith(number)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

}
