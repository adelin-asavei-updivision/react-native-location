#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

#import <CoreLocation/CoreLocation.h>

typedef void (^RNLocationChangeEmitter)(NSArray * _Nonnull value);
typedef void (^RNLocationErrorEmitter)(NSDictionary * _Nonnull value);

@interface RNLocationUtils : NSObject

@property(class, nonatomic, strong, nonnull) NSString *name;

+ (void)setEmitters:(RNLocationChangeEmitter _Nonnull)_onChangeEmitter
           onError:(RNLocationErrorEmitter _Nonnull)_onErrorEmitter;

+ (void)emitChange:(NSArray *_Nonnull)body;
+ (void)emitError:(NSString *_Nonnull)code message:(NSString *_Nonnull)message critical:(BOOL)critical;
+ (void)emitError:(NSString *_Nonnull)code message:(NSString *_Nonnull)message;

+ (void)handleException:(NSException *_Nonnull)exception
                resolve:(nullable RCTPromiseResolveBlock)resolve
                 reject:(nullable RCTPromiseRejectBlock)reject;
+ (void)handleException:(NSException *_Nonnull)exception;

+ (NSDictionary *_Nonnull)locationToMap:(CLLocation *_Nonnull)location;

+ (void)reset;

@end
