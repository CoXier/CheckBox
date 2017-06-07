## CircleCheckBox Screenshot：
<img src="/art/screenshot.gif" alt="screenshot" title="screenshot" width="300" height="533" />

## Attrs

|attr|format|
|:--:|:--:|
|checked|boolean|
|animation_duration|integer|
|tick_width|dimension|
|border_width|dimension|
|tick_color|color|
|border_color|color|
|background_color|color|

## Including in your project
- `CircleCheckBox` is available in the MavenCentral, so getting it as simple as adding it as a dependency
```gradle
compile 'com.uniquestudio:checkbox:1.0.10'
```

## Usage

```java
checkBox = (CircleCheckBox) findViewById(R.id.circle_check_box);
checkBox.setListener(new CircleCheckBox.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(boolean isChecked) {
        // do something
    }
});
```

## License

    Copyright 2016 CoXier

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
