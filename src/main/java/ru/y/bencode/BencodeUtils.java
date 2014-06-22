package ru.y.bencode;

import com.google.common.base.Charsets;

import java.nio.charset.Charset;

/**
 * Created by Denis.Kornienko on 20.06.2014.
 */

//
public class BencodeUtils {

    public static final Charset DEFAULT_CHARSET = Charsets.US_ASCII;

    public static final Character END = 'e';

    //i<integer encoded in base ten ASCII>e
    public static final Character INT = 'i';

    //<length>:<contents>
    public static final Character STRINGDELIM = ':';

    //l<contents>e
    public static final Character LIST = 'l';

    //d<contents>e
    public static final Character DICT = 'd';

}
