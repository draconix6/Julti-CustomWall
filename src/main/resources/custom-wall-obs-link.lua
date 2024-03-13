-- credits to github.com/duncanruns

obs = obslua

---- Variables ----

julti_dir = os.getenv("UserProfile"):gsub("\\", "/") .. "/.Julti/"
timers_activated = false
last_state_text = ""
last_layer_text = ""
last_scene_name = ""

gen_scenes_requested = false
gen_stream_scenes_requested = false

total_width = 0
total_height = 0


-- Constants
ALIGN_TOP_LEFT = 5 -- equivalent to obs.OBS_ALIGN_TOP | obs.OBS_ALIGN_LEFT
ALIGN_CENTER = 0   -- equivalent to obs.OBS_ALIGN_CENTER

---- File Functions ----

function read_first_line(filename)
    local rfile = io.open(filename, "r")
    if rfile == nil then
        return ""
    end
    io.input(rfile)
    local out = io.read()
    io.close(rfile)
    return out
end

function write_file(filename, string)
    local wfile = io.open(filename, "w")
    io.output(wfile)
    io.write(string)
    io.close(wfile)
end

function get_state_file_string()
    local success, result = pcall(read_first_line, julti_dir .. "state")
    if success then
        return result
    end
    return nil
end

function get_layer_file_string()
    local success, result = pcall(read_first_line, julti_dir .. "layerstate")
    if success then
        return result
    end
    return nil
end

---- Misc Functions ----

function split_string(input_string, split_char)
    local out = {}
    -- https://stackoverflow.com/questions/1426954/split-string-in-lua
    for str in input_string.gmatch(input_string, "([^" .. split_char .. "]+)") do
        table.insert(out, str)
    end
    return out
end

---- Obs Functions ----

function get_scene(name)
    local source = get_source(name)
    if source == nil then
        return nil
    end
    local scene = obs.obs_scene_from_source(source)
    release_source(source)
    return scene
end

function get_group_as_scene(name)
    local source = get_source(name)
    if source == nil then
        return nil
    end
    local scene = obs.obs_group_from_source(source)
    release_source(source)
    return scene
end

function remove_source_or_scene(name)
    local source = get_source(name)
    obs.obs_source_remove(source)
    release_source(source)
end

--- Requires release after use
function get_source(name)
    return obs.obs_get_source_by_name(name)
end

function release_source(source)
    obs.obs_source_release(source)
end

function release_scene(scene)
    obs.obs_scene_release(scene)
end

function scene_exists(name)
    return get_scene(name) ~= nil
end

function create_scene(name)
    release_scene(obs.obs_scene_create(name))
end

function switch_to_scene(scene_name)
    local scene_source = get_source(scene_name)
    if (scene_source == nil) then return false end
    obs.obs_frontend_set_current_scene(scene_source)
    release_source(scene_source)
    return true
end

function get_video_info()
    local video_info = obs.obs_video_info()
    obs.obs_get_video_info(video_info)
    return video_info
end

function set_position_with_bounds(scene_item, x, y, width, height, center_align)
    -- default value false
    center_align = center_align or false

    local bounds = obs.vec2()
    bounds.x = width
    bounds.y = height

    if center_align then
        obs.obs_sceneitem_set_bounds_type(scene_item, obs.OBS_BOUNDS_NONE)
        local scale = obs.vec2()
        scale.x = center_align_scale_x
        scale.y = center_align_scale_y
        obs.obs_sceneitem_set_scale(scene_item, scale)
    else
        obs.obs_sceneitem_set_bounds_type(scene_item, obs.OBS_BOUNDS_STRETCH)
        obs.obs_sceneitem_set_bounds(scene_item, bounds)
    end

    -- set alignment of the scene item to: center_align ? CENTER : TOP_LEFT
    obs.obs_sceneitem_set_alignment(scene_item, center_align and ALIGN_CENTER or ALIGN_TOP_LEFT)

    set_position(scene_item, x + (center_align and total_width / 2 or 0), y + (center_align and total_height / 2 or 0))
end

function set_position(scene_item, x, y)
    local pos = obs.vec2()
    pos.x = x
    pos.y = y
    obs.obs_sceneitem_set_pos(scene_item, pos)
end

function set_crop(scene_item, left, top, right, bottom)
    local crop = obs.obs_sceneitem_crop()
    crop.left = left
    crop.top = top
    crop.right = right
    crop.bottom = bottom
    obs.obs_sceneitem_set_crop(scene_item, crop)
end

function get_sceneitem_name(sceneitem)
    return obs.obs_source_get_name(obs.obs_sceneitem_get_source(sceneitem))
end

function bring_to_top(item)
    if item ~= nil then
        obs.obs_sceneitem_set_order(item, obs.OBS_ORDER_MOVE_TOP)
    end
end

function bring_to_bottom(item)
    obs.obs_sceneitem_set_order(item, obs.OBS_ORDER_MOVE_BOTTOM)
end

function delete_source(name)
    local source = get_source(name)
    if (source ~= nil) then
        obs.obs_source_remove(source)
        release_source(source)
    end
end

---- Script Functions ----

function script_description()
    return "<h1>Julti Custom Wall Plugin OBS Link</h1><p>Links OBS to the Julti Custom Wall plugin for craaazy layouts</p>"
end

function script_properties()
    local props = obs.obs_properties_create()
    return props
end

function script_load(settings)
end

function script_update(settings)
    if timers_activated then
        return
    end
    timers_activated = true
    obs.timer_add(loop, 20)
end

function loop()
    -- Scene Change Check

    local current_scene_source = obs.obs_frontend_get_current_scene()
    local current_scene_name = obs.obs_source_get_name(current_scene_source)
    release_source(current_scene_source)
    if last_scene_name ~= current_scene_name then
        last_scene_name = current_scene_name
    end

    -- Check doing stuff too early
    if current_scene_name == nil then
        return
    end

    -- Check on Julti scene before continuing

    local is_on_a_julti_scene = (current_scene_name == "Julti") or (current_scene_name == "Lock Display") or
        (current_scene_name == "Dirt Cover Display") or (current_scene_name == "Walling") or
        (current_scene_name == "Playing") or (string.find(current_scene_name, "Playing ") ~= nil)

    if not is_on_a_julti_scene then
        return
    end

    -- Ensure hidden lock example

    -- Get state output

    local out = get_state_file_string()
    if out ~= nil and last_state_text ~= out then
        last_state_text = out
    else
        return
    end

    -- Process state data

    local data_strings = split_string(out, ";")
    local instance_count = (#data_strings) - 2
    local user_location = nil
    for k, data_string in pairs(data_strings) do
        if user_location == nil then
            user_location = data_string
        end
    end

    if user_location ~= "W" then
        return
    end

    local layer_out = get_layer_file_string()
    if layer_out ~= nil and last_layer_text ~= layer_out then
        last_layer_text = layer_out
    else
        return
    end

    local scene = get_scene("Julti")
    local layer_order = split_string(layer_out, ",")
    for k, inst in pairs(layer_order) do
--         obs.script_log(200, inst)
        bring_to_top(obs.obs_scene_find_source(scene, "Instance " .. inst))
    end
    bring_to_top(obs.obs_scene_find_source(get_scene("Julti"), "Wall On Top"))
end