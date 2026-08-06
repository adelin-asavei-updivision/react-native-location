#import "RNLocationUtils.h"
#import "RNLocationConstants.h"
#import "RNLocationException.h"

static NSString *name = @"RNLocation";
static ChangeEmitter onChangeEmitter = nil;
static ErrorEmitter onErrorEmitter = nil;

@implementation RNLocationUtils

+ (NSString *)name {
    return name;
}

+ (void)setName:(NSString *)_name {
    name = _name;
}

+ (void)setEmitters:(ChangeEmitter)_onChangeEmitter onError:(ErrorEmitter)_onErrorEmitter {
    onChangeEmitter = [_onChangeEmitter copy];
    onErrorEmitter = [_onErrorEmitter copy];
}

+ (void)emitChange:(NSArray *)body {
    if (!onChangeEmitter) return;

    onChangeEmitter(body);
}

+ (void)emitError:(NSString *)code message:(NSString *)message critical:(BOOL)critical {
    if (!onErrorEmitter) return;

    NSMutableDictionary *map = [NSMutableDictionary dictionary];
    map[@"code"] = code;
    map[@"message"] = message;
    map[@"critical"] = @(critical);

    onErrorEmitter(map);
}

+ (void)emitError:(NSString *)code message:(NSString *)message {
    [self emitError:code message:message critical:NO];
}

+ (void)handleException:(NSException *)exception
                resolve:(nullable RCTPromiseResolveBlock)resolve
                 reject:(nullable RCTPromiseRejectBlock)reject {
    BOOL hasPromise = (reject != nil);
    NSString *message = exception.reason ?: RNLocationErrorMessage.UNKNOWN;

    if ([exception isKindOfClass:[RNLocationException class]]) {
        RNLocationException *e = (RNLocationException *)exception;
        if (hasPromise) reject(e.code, message, nil);
        else [self emitError:e.code message:message critical:e.critical];
    } else {
        if (hasPromise) reject(RNLocationError.UNKNOWN, message, nil);
        else [self emitError:RNLocationError.UNKNOWN message:message];
    }
}

+ (void)handleException:(NSException *)exception {
    [self handleException:exception resolve:nil reject:nil];
}

+ (NSDictionary *)locationToMap:(CLLocation *)location {
    NSMutableDictionary *map = [NSMutableDictionary dictionary];
    map[@"latitude"] = @(location.coordinate.latitude);
    map[@"longitude"] = @(location.coordinate.longitude);
    map[@"accuracy"] = @(location.horizontalAccuracy);
    map[@"altitude"] = @(location.altitude);
    map[@"altitudeAccuracy"] = @(location.verticalAccuracy);
    map[@"course"] = @(location.course);
    map[@"speed"] = @(location.speed);
    map[@"floor"] = @(location.floor.level);
    map[@"timestamp"] = @([location.timestamp timeIntervalSince1970] * 1000);
    return map;
}

+ (void)reset {
    name = @"RNLocation";
    onChangeEmitter = nil;
    onErrorEmitter = nil;
}

@end
