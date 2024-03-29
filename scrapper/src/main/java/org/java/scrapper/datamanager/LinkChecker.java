package org.java.scrapper.datamanager;

import org.java.scrapper.dto.link.LinkResponse;

public interface LinkChecker {
    Boolean check(LinkResponse link);
}
