package com.example.assignmentandroidadvanced

import android.util.Log
import org.w3c.dom.CharacterData
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.IOException
import java.io.StringReader
import java.lang.Exception
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory






class XMLParse {
    @Throws(IOException::class, SAXException::class)
    fun getDocument(xml: String?): Document? {
        var document: Document? = null
        var builder: DocumentBuilder? = null
        val factory = DocumentBuilderFactory.newInstance()
        try {
            builder = factory.newDocumentBuilder()
            val inputSource = InputSource()
            inputSource.characterStream = StringReader(xml)
            inputSource.encoding = "UTF-8"
            document = builder!!.parse(inputSource)
            return document

        } catch (e: Exception) {
            Log.e("ERR", e.toString())
        }

        return null
    }

    fun getTitleValue(item: Element, name: String?): String {
        val nodeList = item.getElementsByTagName(name)
        return getTextNodeValue(nodeList.item(0))
    }

    fun getDescriptionValue(item: Element, name: String?):String? {
        val nodeList = item.getElementsByTagName(name)
        return getDescriptionFromCDATA(nodeList.item(0) as Element)
    }

    fun getImageLinkValue(item: Element, name: String?) :String? {
        val nodeList = item.getElementsByTagName(name)
        return getImageLinkFromCDATA(nodeList.item(0) as Element)
    }

    private fun getTextNodeValue(node: Node?): String {
        val child: Node?
        if (node != null) {
            if (node.hasChildNodes()) {
                child = node.firstChild
                while (child != null) {
                    if (child.nodeType == Node.TEXT_NODE) {
                        return child.nodeValue
                    }
                    child.nextSibling
                }
            }
        }
        return ""
    }

    private fun getDescriptionFromCDATA(e: Element): String? {
        val list = e.childNodes
        var data: String
        for (index in 0 until list.length) {
            if (list.item(index) is CharacterData) {
                val child = list.item(index) as CharacterData
                data = child.data

                val lastIndex = data.lastIndexOf(">")

                if(lastIndex > 0) {
                    data = data.substring(lastIndex + 1)
                }

                if (data != null && data.trim { it <= ' ' }.isNotEmpty()) return data
            }
        }
        return ""
    }

    private fun getImageLinkFromCDATA(e: Element) : String {
        val list = e.childNodes
        var data: String
        for (index in 0 until list.length) {
            if (list.item(index) is CharacterData) {
                val child = list.item(index) as CharacterData
                data = child.data

                val firstIndex = data.indexOf("src=\"")
                val lastIndex = data.lastIndexOf("\" >")

                if(lastIndex > 0 && firstIndex > 0) {
                    data = data.substring(firstIndex + 5 , lastIndex)
                }

                if (data != null && data.trim { it <= ' ' }.isNotEmpty()) return data
            }
        }
        return ""
    }


}