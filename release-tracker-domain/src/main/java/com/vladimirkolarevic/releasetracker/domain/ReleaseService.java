package com.vladimirkolarevic.releasetracker.domain;

import java.util.List;

public interface ReleaseService {

     Release save(Release release);

     Release get(String id);

     List<Release> list();

     void delete(String id);

     Long count();

}
