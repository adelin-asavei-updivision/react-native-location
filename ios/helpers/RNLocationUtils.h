#import <Foundation/Foundation.h>

#import <React/RCTBridgeModule.h>

#import <CoreLocation/CoreLocation.h>

typedef void (^ChangeEmitter)(NSArray * _Nonnull value);
typedef void (^ErrorEmitter)(NSDictionary * _Nonnull value);

@interface RNLocationUtils : NSObject

@property(class, nonatomic, strong, nonnull) NSString *name;

+ (void)setEmitters:(ChangeEmitter _Nonnull)_onChangeEmitter
           onError:(ErrorEmitter _Nonnull)_onErrorEmitter;

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
