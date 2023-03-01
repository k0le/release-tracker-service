package com.vladimirkolarevic.releasetracker.domain;

import java.util.List;
import java.util.UUID;

public interface ReleaseService {

     Release save(Release release);

     Release get(UUID uuid);

     List<Release> list();

     void delete(UUID uuid);

     Long count();

}
