//
// Copyright 2012 The Android Open Source Project
//
<<<<<<< HEAD
// Cache for resIds - we tend to lookup the same thing repeatedly
//
=======
// Manage a resource ID cache.
>>>>>>> bd9fa42... Cache resource ID lookups in aapt

#ifndef RESOURCE_ID_CACHE_H
#define RESOURCE_ID_CACHE_H

<<<<<<< HEAD
#include "StringPool.h"

class ResourceIdCache {
public:
    static uint32_t lookup(const String16& package,
                           const String16& type,
                           const String16& name,
                           bool onlyPublic);

    static bool store(const String16& package,
                      const String16& type,
                      const String16& name,
                      bool onlyPublic,
                      uint32_t resId);
};

=======
namespace android {
class android::String16;

class ResourceIdCache {
public:
    static uint32_t lookup(const android::String16& package,
            const android::String16& type,
            const android::String16& name,
            bool onlyPublic);

    static uint32_t store(const android::String16& package,
            const android::String16& type,
            const android::String16& name,
            bool onlyPublic,
            uint32_t resId);

    static void dump(void);
};

}

>>>>>>> bd9fa42... Cache resource ID lookups in aapt
#endif
