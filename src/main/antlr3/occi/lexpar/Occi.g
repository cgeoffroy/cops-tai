/*
Copyright (c) 2008-2011, Intel Performance Learning Solutions Ltd.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of Intel Performance Learning Solutions Ltd. nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Intel Performance Learning Solutions Ltd. BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

grammar Occi;

options {
  language = Java;
}

@header {
    package occi.lexpar;
	import java.util.List;
	import java.util.Set;
    import java.util.Map;
    import java.util.HashMap;
    import java.util.ArrayList;
    import java.math.BigDecimal;
    import java.math.BigInteger;
    import java.net.URI;
    import java.net.URISyntaxException;
    import java.net.URL;
    import java.net.MalformedURLException;
    import antlr.SemanticException;
    
    import org.tai.cops.occi.client.TypeIdentifier;
    import org.tai.cops.occi.client.Category;
    import org.tai.cops.occi.client.Kind;
    
	import com.google.common.collect.Sets;
}

@lexer::header {
  package occi.lexpar;
}

@members{

  public static String occi_categories  = "occi.categories";
  public static String occi_links       = "occi.links";
  public static String occi_attributes  = "occi.attributes";
  public static String occi_locations   = "occi.locations";
  static String occi_core_term          = "occi.core.term";
  static String occi_core_scheme        = "occi.core.scheme";
  static String occi_core_class         = "occi.core.class";
  static String occi_core_class_kind    = "kind";
  static String occi_core_class_mixin   = "mixin";
  static String occi_core_class_action  = "action";
  static String occi_core_title         = "occi.core.title";
  static String occi_core_rel           = "occi.core.rel";
  static String occi_core_location      = "occi.core.location";
  static String occi_core_attributes    = "occi.core.attributes";
  static String occi_core_actions       = "occi.core.actions";
  static String occi_core_target        = "occi.core.target";
  static String occi_core_actionterm    = "occi.core.actionterm";
  static String occi_core_self          = "occi.core.self";
  static String occi_core_category      = "occi.core.category";

  private String last_error = "";

  //private HashMap allheaders = new HashMap();

  /*private IErrorReporter errorReporter = null;

  protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow){
    System.out.println("recoverFromMismatchedToken");
    return null;
  }

  public void setErrorReporter(IErrorReporter errorReporter) {
    this.errorReporter = errorReporter;
  }

  public void emitErrorMessage(String msg) {
    errorReporter.reportError(msg);
  }*/

  public static OcciParser getParser(String occiHeader) throws Exception {

 //   IErrorReporter errorReporter = new ErrorReporter();

    CharStream stream = new ANTLRStringStream(occiHeader);
	  OcciLexer lexer = new OcciLexer(stream);
//	  lexer.setErrorReporter(errorReporter);

	  CommonTokenStream tokenStream = new CommonTokenStream(lexer);
	  OcciParser parser = new OcciParser(tokenStream);
	  //parser.setErrorReporter(errorReporter);

    return parser;
  }

  public String getLastError(){
    return last_error;
  }

  private String removeQuotes(String cleanMe){

    if(cleanMe.matches("^(\"|').*(\"|')$"))
      cleanMe = cleanMe.substring(1, cleanMe.length()-1);

    return cleanMe;
  }

//  public void emitErrorMessage(String message){
//    System.out.println("A emitErrorMessage occured");
//  }

//  public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
//    System.out.println("A displayRecognitionError occured");
//  }
}

@rulecatch{
  catch(RecognitionException rex) {

    last_error = getErrorHeader(rex) + " " + getErrorMessage(rex, OcciParser.tokenNames);
    //System.out.println("Parser error: " + last_error);

    throw new RuntimeException(last_error);
  }
}

headers                returns [HashMap value] :
                         {
                           $value = new HashMap();
                           List<Category> catList = new ArrayList();
                           ArrayList linkList = new ArrayList();
                           Map<String, Object> attrList = new HashMap<>();
                           List<URL> locList = new ArrayList<URL>();
                         }
                         ((
                           category  { if($category.cats != null) catList.addAll($category.cats); } |
                           link      { if($link.link != null) linkList.add($link.link); } |
                           attribute { if($attribute.attrs != null) attrList.putAll($attribute.attrs); } |
                           location  { if($location.urls != null) locList.addAll($location.urls); }
                         ) (EOF | '\n') )*
                         {
                           $value.put(occi_categories, catList);
                           $value.put(occi_links, linkList);
                           $value.put(occi_attributes, attrList);
                           $value.put(occi_locations, locList);
                         }
                         ;

/*
  e.g.
  Category: storage; \
    scheme="http://schemas.ogf.org/occi/infrastructure#"; \
    class="kind"; \
    title="Storage Resource"; \
    rel="http://schemas.ogf.org/occi/core#resource"; \
    location=/storage/; \
    attributes="occi.storage.size occi.storage.state"; \
    actions="http://schemas.ogf.org/occi/infrastructure/storage/action#resize"
*/

category               returns [List<Category> cats] :
                         'Category' ':'
                         category_values ';'?{
                           $cats = $category_values.cats;
                         }
                         ;

category_values        returns [List<Category> cats] :
	                       cv1=category_value{
	                         $cats = new ArrayList();
	                         $cats.add($cv1.cat);
	                       }
	                       (
	                       ',' cv2=category_value{
	                             $cats.add($cv2.cat);
	                           }
	                       )*
	                       ;

category_value         returns [Category cat] :
	                       term_attr scheme_attr klass_attr title_attr? rel_attr? c_attributes_attr? actions_attr? location_attr? {
	                         switch ($klass_attr.value) {
	                         	case "kind":
	                         	    $cat = new Kind($term_attr.value, $scheme_attr.value,
	                         	    	$title_attr.value, $location_attr.value,
	                         			$c_attributes_attr.value, $actions_attr.value,
	                         			$rel_attr.value);
	                         		break;
	                         	case "action":
	                         	case "mixin":
	                         		$cat = new Category($term_attr.value, $scheme_attr.value,
	                         	    	$klass_attr.value, $title_attr.value,
	                         	    	$location_attr.value, $c_attributes_attr.value);
	                         	    break;
	                         	default:
	                         	    throw (new RuntimeException(new SemanticException("Detected an invalid category class field: " + $klass_attr.value)));
	                         }
	                         
	                        
	                         

	                    /*     $cat.put(occi_core_term, $term_attr.value);
	                         $cat.put(occi_core_scheme, $scheme_attr.value.toString());
	                         $cat.put(occi_core_class, $klass_attr.value);

                           if($title_attr.value !=null)
                              $cat.put(occi_core_title, $title_attr.value);

                           if($rel_attr.value != null)
                              $cat.put(occi_core_rel, $rel_attr.value.toString());

                           if($location_attr.value != null)
                              $cat.put(occi_core_location, $location_attr.value);

                           if($c_attributes_attr.value != null)
                              $cat.put(occi_core_attributes, $c_attributes_attr.value);

                           if($actions_attr.value != null)
                              $cat.put(occi_core_actions, $actions_attr.value);*/
	                       }
                         ;

term_attr              returns [String value] :
	                       CLASS_VALUE {
	                         $value = $CLASS_VALUE.text;
	                       }
	                       | TERM_VALUE {
	                         $value = $TERM_VALUE.text;
	                       }
	                       ;

//this value can be passed on to the uri rule in Location for validation
scheme_attr            returns [URI value]:// throws SemanticException:
	                       ';' 'scheme' '=' quoted_uri {
	                         $value = $quoted_uri.uri;
	                       }
	                       ;

klass_attr             returns [String value] :
	                       ';' 'class' '=' (
                             c1=CLASS_VALUE {
                               $value = removeQuotes($c1.text);
                             }
                           | QUOTE c2=CLASS_VALUE QUOTE {
                               $value = removeQuotes($c2.text);
                             }
                           )
                           ;

title_attr             returns [String value] :
	                       ';' 'title' '='
	                       QUOTED_VALUE{
	                         $value = removeQuotes($QUOTED_VALUE.text);
	                       }
	                       ;

//this value can be passed on to the uri rule in Location for validation
rel_attr               returns [TypeIdentifier value] :
	                       ';' 'rel' '=' quoted_uri {
	                         $value = new TypeIdentifier($quoted_uri.uri);
	                       }
	                       ;


//this value can be passed on to the uri rule in Location for validation
location_attr          returns [URI value] :
	                       ';' 'location' '=' quoted_uri {
	                         $value = $quoted_uri.uri;
	                       }
	                       ;

//these value once extracted can be passed on to the attributes_attr rule
c_attributes_attr      returns [Set<String> value] :
	                       ';' 'attributes' '='
	                       quoted_values {
	                         $value = $quoted_values.vs;
	                       }
	                       ;

//this value can be passed on to the uri rule in Location for validation
actions_attr           returns [List<TypeIdentifier> value] :
	                       ';' 'actions' '='
	                       quoted_uris {
	                         $value = new ArrayList<>();
	                         for(URI u : $quoted_uris.uris) {
	                           $value.add(new TypeIdentifier(u));
	                         }
	                       }
	                       ;

/* e.g.
        Link: \
          </storage/disk03>; \
          rel="http://example.com/occi/resource#storage"; \
          self="/link/456-456-456"; \
          category="http://example.com/occi/link#disk_drive"; \
          com.example.drive0.interface="ide0", com.example.drive1.interface="ide1"
*/

link                   returns [ArrayList link] :
                         'Link' ':'
                          link_values {
                            $link = $link_values.links;
                          }
                          ;

link_values            returns [ArrayList links] :
                         lv1=link_value {
                           $links = new ArrayList();
                           $links.add($lv1.linkAttrs);
                         }
                         (
                           ',' lv2=link_value{
                                 $links.add($lv2.linkAttrs);
                               }
                         )*
                         ;

link_value             returns [HashMap linkAttrs] :
                         target_attr rel_attr self_attr? category_attr? attribute_attr?
                         {
                           $linkAttrs = new HashMap();

                           $linkAttrs.put(occi_core_target, $target_attr.targetAndTerm.get(0));
                           if($target_attr.targetAndTerm.size() == 2)
                             $linkAttrs.put(occi_core_actionterm, $target_attr.targetAndTerm.get(1));

                           $linkAttrs.put(occi_core_rel, $rel_attr.value);

                           if($self_attr.value != null)
                             $linkAttrs.put(occi_core_self, $self_attr.value);

                           if($category_attr.value != null)
                             $linkAttrs.put(occi_core_category, $category_attr.value);

                           if($attribute_attr.attr != null)
                             $linkAttrs.putAll($attribute_attr.attr);
                         }
                         ;

target_attr            returns [ArrayList targetAndTerm] :
                         '<' TARGET_VALUE {
                               $targetAndTerm = new ArrayList();
                               $targetAndTerm.add($TARGET_VALUE.text);
                             }
                         ('?action='
                           TERM_VALUE{
                             $targetAndTerm.add($TERM_VALUE.text);
                           }
                         )? '>'
                         ;

self_attr              returns [String value] :
                         ';' 'self' '='
                         QUOTED_VALUE{
                           $value = removeQuotes($QUOTED_VALUE.text);
                         }
                         ;

category_attr          returns [String value] :
                         ';' 'category' '='
                         QUOTED_VALUE {
                           $value = removeQuotes($QUOTED_VALUE.text);
                         }
                         ;

attribute_attr         returns [HashMap attr] :
                        ';' attributes_attr {
                            $attr = (HashMap) $attributes_attr.attrs;
                        }
                        ;

attributes_attr        returns [Map<String, Object> attrs] :
                         kv1=attribute_kv_attr {
                               $attrs = new HashMap<String, Object>();
                               $attrs.put((String) $kv1.keyval.get(0), $kv1.keyval.get(1));
                             }
                         (
                           ',' kv2=attribute_kv_attr {
                                     $attrs.put((String) $kv2.keyval.get(0), $kv2.keyval.get(1));
                                   }
                         )*
                         ;

attribute_kv_attr      returns [ArrayList keyval] :
                         attribute_name_attr '=' attribute_value_attr {
                           $keyval = new ArrayList();
                           $keyval.add($attribute_name_attr.text);
                           $keyval.add($attribute_value_attr.value);
                         }
                         ;

//See https://github.com/dizz/occi-grammar/issues#issue/3
attribute_name_attr    : TERM_VALUE;// ('.' TERM_VALUE)* ;

attribute_value_attr   returns [Object value] :
                        QUOTED_VALUE {
                          $value = removeQuotes($QUOTED_VALUE.text);
                        }
                        | DIGITS {
                          $value = new BigInteger(removeQuotes($DIGITS.text));
                        }
                        | FLOAT {
                          $value = new BigDecimal(removeQuotes($FLOAT.text));
                        }
                        ;

/*
e.g.
  X-OCCI-Attribute: \
    occi.compute.architechture="x86_64", \
    occi.compute.cores=2, \
    occi.compute.hostname="testserver", \
    occi.compute.speed=2.66, \
    occi.compute.memory=3.0, \
    occi.compute.state="active"
*/
attribute              returns [Map<String, Object> attrs] :
                         'X-OCCI-Attribute' ':'
                         attributes_attr{
                           $attrs = $attributes_attr.attrs;
                         }
                         ;
/*
e.g.
  X-OCCI-Location: \
    http://example.com/compute/123, \
    http://example.com/compute/456
*/
location               returns [List<URL> urls] :
                         'X-OCCI-Location' ':'
                         location_values{
                           $urls = $location_values.urls;
                         }
                         ;

location_values        returns [List<URL> urls]
						@init{ List<String> _tmp = new ArrayList<String>(); }:
	                    	(u1=( options {greedy=false;} : . )+ {
	                         	$urls = new ArrayList<URL>();
	                         	_tmp.add($u1.text);
	                       	}
	                       	(
	                         ','
	                         u2=( options {greedy=false;} : . )+ {
	                           	_tmp.add($u2.text);
	                         }
	                       	)*) {
	                       		for(String elt : _tmp) {
	                       			try {
	                       				$urls.add(new URL(removeQuotes(elt)));
	                       			} catch (MalformedURLException z) {
	    								throw (new RuntimeException(new SemanticException("Detected an invalid URL: " + z.toString())));
	    							}
	                       		}
	                       	
	                       	}
	                       ;

quoted_values	returns [Set<String> vs]:
	QUOTED_VALUE {
		String tmp = removeQuotes($QUOTED_VALUE.text);
		String[] z = tmp.split("( |\t)+");
		vs = Sets.newHashSet(z);
	};

quoted_uri    returns [URI uri]:
	QUOTED_VALUE {
		try {
	    	$uri = new URI(removeQuotes($QUOTED_VALUE.text));
	    } catch (URISyntaxException z) {
	    	throw (new RuntimeException(new SemanticException("Detected an invalid URI: " + z.toString())));
	    }
	};
	
quoted_uris     returns [List<URI> uris]:
	QUOTED_VALUE {
		try {
			$uris = new ArrayList<>();
			String tmp = removeQuotes($QUOTED_VALUE.text);
			String[] z = tmp.split("( |\t)+");
			for(String s : z) {
				$uris.add(new URI(s));
			}
	    } catch (URISyntaxException z) {
	    	throw (new RuntimeException(new SemanticException("Detected an invalid URI: " + z.toString())));
	    }
	};
	
CLASS_VALUE   : ('kind' | 'mixin' | 'action');
URL           : ( 'http://' | 'https://' )( 'a'..'z' | 'A'..'Z' | '0'..'9' | '@' | ':' | '%' | '_' | '\\' | '+' | '.' | '~' | '#' | '?' | '&' | '/' | '=' | '-')*;
DIGITS        : ('0'..'9')* ;
FLOAT         : ('0'..'9' | '.')* ;
QUOTE         : ('"'|'\'') ;
TERM_VALUE    : ('a'..'z' | 'A'..'Z' | '0'..'9' | '-' | '_' | '.')+;
TARGET_VALUE  : ('a'..'z' | 'A'..'Z' | '0'..'9' | '/' | '-' | '_')* ;
QUOTED_VALUE  : QUOTE ( options {greedy=false;} : . )* QUOTE ;
WS  :   ( ' ' | '\t' | '\r' | '\n' ) {$channel=HIDDEN;} ;

