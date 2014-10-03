var cardDom = "<div class=\"card card9 line-around\">";

var headerDom = "<header class=\"layout-box media-graphic\">";
var sectionDom = "<section class=\"weibo-detail\">";
var footerDom = "<footer class=\"more-detail line-top layout-box box-center-v\">";

function addWeiboCard(jsonObj) {
	var newHtml = "";
	for (var i = 0; i < 10; i++) {
		newHtml += cardDom;
		// ========== Header Begin
		newHtml += headerDom;
		newHtml += "<a href=\"/u/1998370765\" class=\"mod-media size-xs\">";
		newHtml += "	<div class=\"media-main\">";
		newHtml += "		<img width=\"34\" height=\"34\" src=\"http://tp2.sinaimg.cn/1998370765/50/40039495395/0\">";
		newHtml += "	</div>";
		newHtml += "</a>";
		newHtml += "<div class=\"box-col item-list\">";
		newHtml += "	<a href=\"/u/1998370765\" class=\"item-main txt-l mct-a txt-cut\"> <span>那些触心的小情书</span>";
		newHtml += "		<i class=\"icon icon-vip\"></i>";
		newHtml += "	</a>";
		newHtml += "	<div class=\"item-minor txt-xxs mct-d txt-cut\">";
		newHtml += "		<span class=\"time\">4分钟前</span> <span class=\"from\">来自皮皮时光机</span>";
		newHtml += "	</div>";
		newHtml += "</div>";
		newHtml += "<a class=\"operate-box\" data-act-type=\"hover\"> <i class=\"icon-font icon-font-arrow-down txt-s\"></i>";
		newHtml += "</a>";
		newHtml += "</header>";
		// ========== Header End
		// ========== Section Begin
		newHtml += sectionDom;
		newHtml += "<p class=\"default-content txt-xl\">每个都有feel，对时尚没有抵抗力</p>";
		newHtml += "<div class=\"extend-content\">";
		newHtml += "<div class=\"inner\">";
		newHtml += "<p class=\"weibo-original txt-l\">";
		newHtml += "<a href=\"/u/2212649322\" class=\"\">@热门时尙潮流</a>：时尚手绘服装 求围观&gt;&gt;<i class=\"face face_1 icon_4\">[爱你]</i>想第一时间了解最全的时尚潮流前沿，请关注<a href=\"/n/%E7%83%AD%E9%97%A8%E6%97%B6%E5%B0%99%E6%BD%AE%E6%B5%81\">@热门时尙潮流</a>";
		newHtml += "</p>";
		newHtml += "<div class=\"media-pic-list\">";
		newHtml += "<ul>";
		newHtml += "<li><img class=\"loaded\" src=\"http://ww2.sinaimg.cn/thumb180/83e2596agw1e9hhecf0a7j20c80haq3u.jpg\" data-node=\"pic\" data-act-type=\"hover\"></li>";
		newHtml += "<li><img class=\"loaded\" src=\"http://ww2.sinaimg.cn/thumb180/83e2596agw1e9hhee8bxwj20c80h9q40.jpg\" data-node=\"pic\" data-act-type=\"hover\"></li>";
		newHtml += "</ul>";
		newHtml += "</div>";
		newHtml += "</div>";
		newHtml += "</div>";
		newHtml += "</section>";
		// ========== Section End
		// ========== Footer Begin
		newHtml += footerDom;
		newHtml += "<a href=\"javascript:void(0);\" class=\"box-col txt-s\" data-act-type=\"hover\" data-node=\"forward\"><i class=\"icon-font icon-font-forward\"></i><em class=\"num mct-d\">转发</em></a>";
		newHtml += "<i class=\"line-gradient\"></i> <a href=\"javascript:void(0);\" class=\"box-col txt-s\" data-act-type=\"hover\" data-node=\"comment\"><i class=\"icon-font icon-font-comment\"></i><em class=\"num mct-d\">评论</em></a>";
		newHtml += "<i class=\"line-gradient\"></i> <a href=\"javascript:void(0);\" class=\"box-col txt-s\" data-act-type=\"hover\" data-node=\"like\"><i class=\"icon icon-likesmall\"></i><em class=\"num mct-d\">赞</em></a>";
		newHtml += "</footer>";
		// ========== Footer End
		newHtml += "</div>";

	}

	$("#weibo_content").html(newHtml);
}
