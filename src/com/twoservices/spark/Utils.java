package com.twoservices.spark;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.widget.Toast;

import com.twoservices.spark.link.Feedback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	/**
	 * Flag that decides whether debug mode or not
	 */
	public static final boolean DEBUG = false;
	
	/**
	 * Get the various styled welcome string
	 * 
	 * @return Spanned String
	 */
	public static Spanned getFormattedWelcomeString(String text) {
		if (TextUtils.isEmpty(text)) return new SpannableString("");
		
		String strongStr = AccountInfo.sProjectName;
		int pos = text.indexOf(strongStr);
		
		if (pos != -1) {
			String prefix = text.substring(0, pos);
			String suffix = "";

			if (text.length() > (pos + strongStr.length()))
				suffix = text.substring(pos + strongStr.length());

			return Html.fromHtml(prefix + " <b><font color=\"#000000\">"
					+ strongStr + "</font></b> " + suffix);
		} else {
			return new SpannableString(text);
		}
	}

	/**
	 * Get the string that begin to <i>"my"</i>
	 * 
	 * @param str
	 *            String that will convert
	 * @return SpannableString
	 */
	public static SpannableString getMyString(String str) {
		SpannableString ss;
		
		if (str.indexOf("my", 0) == 0 || str.indexOf("My", 0) == 0) {
	        ss = new SpannableString("my" + str.substring(2));
			ss.setSpan(new StyleSpan(Typeface.ITALIC), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			ss = new SpannableString(str);
		}
			
		return ss;
	}
	
	/**
	 * Convert the string to display in section bar
	 * 
	 * @param text
	 *            string will converted by new formatter
	 * @return Date format string
	 */
	public static String toMyDateFormat(String text) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM, dd yyyy hh:mm:ss");
		
		Date date = new Date();
		try {
			date = formatter.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		formatter = new SimpleDateFormat("EEE, MMM, dd, yyyy");
		return formatter.format(date).toString();
	}
	
	/**
	 * Get the string that begin to "Booth "
	 * 
	 * @param str
	 *            String that will convert
	 * @return SpannableString
	 */
	public static SpannableString getBoothString(String str) {
		SpannableString ss;
		
		if (TextUtils.isEmpty(str)) {
			ss = new SpannableString("");
		} else {
			ss = new SpannableString("Booth " + str);
			ss.setSpan(new StyleSpan(Typeface.BOLD), 6, 6 + str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
			
		return ss;
	}
	
	/**
	 * Get the string that are represents "<b>boldStr</b> normalStr"
	 * 
	 * @param boldStr
	 * @param normalStr
	 *            
	 * @return SpannableString
	 */
	public static SpannableString getBoldNormalString(String boldStr, String normalStr) {
		SpannableString ss;
		
		if (TextUtils.isEmpty(boldStr)) {
			ss = new SpannableString(normalStr);
		} else {
			int pos = boldStr.length();

			ss = new SpannableString(boldStr + " " + normalStr);
			ss.setSpan(new StyleSpan(Typeface.BOLD), 0, pos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
			
		return ss;
	}
	
	/**
	 * Get the string that are represents "normalStr <b>boldStr</b>"
	 * 
	 * @param boldStr
	 * @param normalStr
	 *            
	 * @return SpannableString
	 */
	public static SpannableString getNormalBoldString(String normalStr, String boldStr) {
		SpannableString ss;
		int pos = normalStr.length();
		
        ss = new SpannableString(normalStr + " " + boldStr);
		ss.setSpan(new StyleSpan(Typeface.BOLD), pos, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		return ss;
	}
	
	/**
	 * Get the string that are represents "normalStr <b>boldStr</b>"
	 * 
	 * @param normalStr
	 * @param boldStr
	 * @param normalStr2
	 * 
	 * @return SpannableString
	 */
	public static SpannableString getNormalBoldNormalString(String normalStr, String boldStr, String normalStr2) {
		SpannableString ss;
		int start = normalStr.length();
		int end = boldStr.length();
		
        ss = new SpannableString(normalStr + " " + boldStr + ", " + normalStr2);
		ss.setSpan(new StyleSpan(Typeface.BOLD), start + 1, start + end + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		return ss;
	}
	
	/**
	 * Get the string that begin to "Booth "
	 * 
	 * @param postTime
	 *            String that will convert
	 * @return SpannableString
	 */
	public static SpannableString getPostTimeString(String postTime) {
		SpannableString ss;
		
        ss = new SpannableString("Posted " + postTime);
		ss.setSpan(new StyleSpan(Typeface.BOLD), 7, 7 + postTime.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			
		return ss;
	}
	
	/**
	 * Get Time format string
	 * 
	 * @param time
	 *            String that represented by "hh:mm:ss" format
	 * @return SpannableString
	 */
	public static SpannableString getTimeString(String time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		
		Date date = new Date();
		try {
			if (!TextUtils.isEmpty(time))
				date = formatter.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		formatter = new SimpleDateFormat("h:mma");
		return new SpannableString(formatter.format(date).toString());
	}
	
	/**
	 * Get Time format string
	 * 
	 * @param start
	 * @param end
	 * 
	 * @return SpannableString
	 */
	public static SpannableString getTimeDurationString(String start, String end) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		
		Date startDate = new Date();
		Date endDate = new Date();
		
		try {
			startDate = formatter.parse(start);
			endDate = formatter.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		formatter = new SimpleDateFormat("h:mm a");
		
		return new SpannableString(formatter.format(startDate).toString()
				+ " - " + formatter.format(endDate).toString());
	}
	
	/**
	 * Get time format string from date format string
	 * 
	 * @param strDate
	 *            Date string will converted
	 * @return String
	 */
	public static String getTimeFromDateString(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM, dd yyyy HH:mm:ss");
		
		Date date = new Date();
		try {
			if (!TextUtils.isEmpty(strDate))
				date = formatter.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		formatter = new SimpleDateFormat("h:mm a");
		return formatter.format(date).toString();
	}
	
	/**
	 * Get hyper-linked text
	 * 
	 * @param dispStr
	 *            display string
	 * @param filename
	 *            name of file
	 * @param filesize
	 *            size of file
	 * @return hyper-link text
	 */
	public static Spanned getLinkedFileText(String dispStr, String filename, String filesize) {
		if (TextUtils.isEmpty(filename)) return new SpannableString("");
		
		if (TextUtils.isEmpty(filesize)) {
			filesize = "";
		} else {
			filesize = " (" + filesize + ")";
		}
			
		String addr = Feedback.HTTP_ROOT_URL + "/collateral/" + AccountInfo.sProjectId + "/" + filename;
		
		return Html.fromHtml("<a href=\"" + addr + "\">" + dispStr + "</a>" + filesize);
	}
	
	/**
	 * Get hyper-linked text
	 * 
	 * @param addr
	 *            destination address
	 * @param prefixStr
	 *            prefix string            
	 * @param dispStr
	 *            display string
	 * @param suffixStr
	 *            suffix string           
	 * @return hyper-link text
	 */
	public static Spanned getLinkedText(String addr, String prefixStr, String dispStr, String suffixStr) {
		if (TextUtils.isEmpty(dispStr)) return new SpannableString("");
		
		return Html.fromHtml(prefixStr + " <a href=\"" + addr + "\"><b>" + dispStr + "</b></a>" + suffixStr);
	}
	
	/**
	 * Get API string that will run on web service
	 * 
	 * @param action
	 *            action string such as "Add", "Remove" etc.
	 * @param idType
	 *            id type such as "uu_session_id", "uu_exhibitor_id"
	 * @param id
	 *            object id of event such as "Agenda", "Exhibitors", "Collateral".
	 * @return String
	 */
	public static String getActionAPIString(String action, String idType, String id) {
		StringBuilder sbAPI = new StringBuilder();

		sbAPI.append(Feedback.HTTP_URL).append("?action=").append(action);
		
		if (!TextUtils.isEmpty(idType))
			sbAPI.append("&").append(idType).append("=").append(id);
		
		sbAPI.append("&UU_PROJECT_ID=").append(AccountInfo.sProjectId)
				.append("&UU_CONTACT_ID=").append(AccountInfo.sContactId);

		return sbAPI.toString();
	}

	public static void informMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	public static void logout(Context context) {
		context.startActivity(new Intent(context, LoginActivity.class));
	}
	
}
