<?php
$site_path="/home/yousmiby/public_html/";
# подключим необходимые для работы файлы
define( 'DB_HOST', 'localhost');
define( 'DB_USER', 'yousmiby_usr');
define( 'DB_PASSWORD', '7vH8sjN58P');
define( 'DB_BASENAME', 'yousmiby_db');
define( 'DB_PREFIX', '');
define( 'DB_SQL_MODE', false);

$html=array("&rdquo;","&ldquo;","&laquo;","&raquo;","&quot;","&gt;","&lt;","&#x201C;","&#x201D;","&#147;","&#148;","&nbsp;","&hellip;","&prime;","&Prime;","&minus;","&lowast","&#146;","&#150;","&#151;","&mdash;","&ndash;","&lsquo;","&rsquo;","<p class=\"\"MsoNormal\"\" style=\"\"TEXT-ALIGN: justify\"\">","<p class=\"\"MsoNormal\"\" style=\"\"text-align: justify\"\">","<p class=\"\"MsoNormal\"\">","&#x2013;", "alt=\"\"","&#x2013;"," style=
\"\"mso-bidi-font-style: normal\"\"","&#8470;","&#x2026","&#133;","&");
$nhtml=array("\"","\"","\"","\"","\"",">","<","\"","\"","\"","\""," ","...","'","\"","-","*","'","-","-","-","-","'","'","<p>","<p>","<p>","-", "alt=\"\"\"\"", "-","","№","...","...","&amp;");



$link = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD) or die("Не могу подключиться" );
mysql_select_db(DB_BASENAME, $link) or die ('Не могу выбрать БД');

$cat_all= 'SELECT * FROM `articles` ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_all_cat= 'SELECT articles.*, categories.title as catname FROM articles, categories WHERE articles.id_category=categories.id ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_all_yandex= 'SELECT * FROM `articles` ORDER BY `articles`.`start_published` DESC LIMIT 0 , 30';
$cat_sport = 'SELECT * FROM `articles` WHERE `id_category` =55 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_auto = 'SELECT * FROM `articles` WHERE `id_category` =56 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_business = 'SELECT * FROM `articles` WHERE `id_category` =60 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_love = 'SELECT * FROM `articles` WHERE `id_category` =61 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_technics = 'SELECT * FROM `articles` WHERE `id_category` =63 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_polezno = 'SELECT * FROM `articles` WHERE `id_category` =64 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_society = 'SELECT * FROM `articles` WHERE `id_category` =68 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_belarus = 'SELECT * FROM `articles` WHERE `id_category` =71 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_skazki = 'SELECT * FROM `articles` WHERE `id_category` =70 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_stihi = 'SELECT * FROM `articles` WHERE `id_category` =85 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_novell = 'SELECT * FROM `articles` WHERE `id_category` =49 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_art = 'SELECT * FROM `articles` WHERE `id_category` =67 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';
$cat_foto = 'SELECT * FROM `articles` WHERE `id_category` =80 ORDER BY `articles`.`start_published` DESC LIMIT 0 , 10';

/*Спорт  и здоровье*/
$result=mysql_query($cat_sport);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_sport.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;
$rssfile=NULL;
$result=NULL;
/*Авто*/
$result=mysql_query($cat_auto);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_auto.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);
/*Бизнес и работа*/
$result=mysql_query($cat_business);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_business.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	
/*Отношения*/
$result=mysql_query($cat_love);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_love.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	
/*Техника и интернет*/
$result=mysql_query($cat_technics);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_technics.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);
/*Полезно*/
$result=mysql_query($cat_polezno);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_polezno.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	
/*Общество*/
$result=mysql_query($cat_society);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_society.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	
/*По-беларуску*/
$result=mysql_query($cat_belarus);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_belarus.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	
/*Сказки*/
$result=mysql_query($cat_skazki);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_skazki.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	
/*Стихи*/
$result=mysql_query($cat_stihi);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_stihi.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	
/*Новеллы и очерки*/


$result=mysql_query($cat_novell);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_novell.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	
/*Арт и культура*/
$result=mysql_query($cat_art);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_art.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	
/*Фоторепортажи*/
$result=mysql_query($cat_foto);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_foto.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	

/*Все*/
$result=mysql_query($cat_all_cat);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";


		/*$query = mysql_query("SELECT a.*, c.title as ctitle FROM articles a, categories c WHERE a.id_category=c.id ORDER BY a.start_published DESC LIMIT 0, 10");
		while ($row = mysql_fetch_array($query)) {
			echo $row["ctitle"]."<br/>";
		}

		echo "\n";
		echo $cat_all_cat;

		echo "\n";*/

		while($r=mysql_fetch_array($result)) 
		{

		$a_date=$r['start_published'];

		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);



		/*$r['articletext'] = str_replace($html, $nhtml, $r['articletext']);
		$r['articletext']=strip_tags($r['articletext']);*/
		$r['articletext']=htmlspecialchars($r['articletext']);
		/*$r['articletext']=htmlentities($r['articletext']);*/
		/*$r['articletext']=html_entity_decode($r['articletext']);*/


		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";

		$rss .= '<category>'.$r['catname'].'</category>' . "\n";

		$rss .= '<description>'.$r['articletext'].'</description>' . "\n";

		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_all.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	

/*Все корокие*/
$result=mysql_query($cat_all);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars_decode($r['description']);
		$r['description']=html_entity_decode($r['description']);
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.filesize($site_path.'images/content/'.$r['image']).'"/>' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	

/*YANDEX-GOOGLE*/
$result=mysql_query($cat_all_yandex);
$rss=NULL;
		$rss .= '<?xml version="1.0" encoding="windows-1251"?>' . "\n";
		$rss .= '<rss version="2.0">' . "\n";
		$rss .= '<channel>' . "\n";
		$rss .= '<title>YOUSMI: Новостная социальная сеть</title>' . "\n";
		$rss .= '<link>http://yousmi.by/</link>' . "\n";
		$rss .= '<description>Новостная социальная сеть</description>' . "\n";
while($r=mysql_fetch_array($result)) 
		{
		$a_date=$r['start_published'];
		$r['description'] = str_replace($html, $nhtml, $r['description']);
		$r['description']=strip_tags($r['description']);
		$r['description']=htmlspecialchars($r['description']);
		//$r['description']=html_entity_decode($r['description']);
		
		$r['articletext'] = str_replace($html, $nhtml, $r['articletext']);
		$r['articletext']=strip_tags($r['articletext']);
		$r['articletext']=htmlspecialchars($r['articletext']);

		
		$rss .= '<item>' . "\n";
		$rss .= '<title>'.$r['title'].'</title>' . "\n";
		$rss .= '<link>http://yousmi.by/articles/'.$r['id'].'/</link>' . "\n";
		$rss .= '<guid>http://yousmi.by/articles/'.$r['id'].'/</guid>' . "\n";
		$rss .= '<description>'.$r['description'].'</description>' . "\n";
		if ($r['articletext']>0) {
		$rss .= '<yandex:full-text>' . $r['articletext'] . '</yandex:full-text>' . "\n";
		}
		$filesize=filesize($site_path.'images/content/'.$r['image']);
		if ($filesize==0) {$filesize=1000;}
		$rss .= '<enclosure url="http://yousmi.by/images/content/'.$r['image'].'"' . ' type="image/jpeg" length="'.$filesize.'" />' . "\n";
		$rss .= '<pubDate>' . date ("D, d M Y H:i:s +0200",mktime(substr($a_date, 11, 2)+0,substr($a_date, 14, 2)+0,0,substr($a_date, 5, 2),substr($a_date, 8, 2),substr($a_date, 0,4))) . '</pubDate>' . "\n";
		$rss .= '</item>' . "\n";
		}
$rss .= '</channel></rss>' . "\n";

$rssfile = fopen($site_path."rss/rss_full.xml","w+"); 
fwrite($rssfile,$rss."\n");
fclose ($rssfile);
$rss=NULL;unset($rssfile);	


echo "ok";
	
?>

