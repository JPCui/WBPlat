/**
 * 获取url中的参数
 * 
 * @param param
 *            参数名
 * @returns 参数值
 */
function getParameter(param) {
	var query = window.location.search;// 获取URL地址中？后的所有字符
	var iLen = param.length;// 获取你的参数名称长度
	var iStart = query.indexOf(param);// 获取你该参数名称的其实索引
	if (iStart == -1)// -1为没有该参数
		return "";
	iStart += iLen + 1;
	var iEnd = query.indexOf("&", iStart);// 获取第二个参数的其实索引
	if (iEnd == -1)// 只有一个参数
		return query.substring(iStart);// 获取单个参数的参数值
	return query.substring(iStart, iEnd);// 获取第二个参数的值
}